package com.authorization_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.authorization_server.config.custom.PublicClientRefreshTokenAuthenticationConverter;
import com.authorization_server.config.custom.PublicClientRefreshTokenAuthenticationProvider;
import com.authorization_server.config.custom.PublicClientRefreshTokenGenerator;
import com.authorization_server.repository.*;
import com.authorization_server.security.*;
import com.authorization_server.service.JpaOAuth2AuthorizationConsentService;
import com.authorization_server.service.JpaOAuth2AuthorizationService;
import com.authorization_server.util.Constants;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    ClientRepository clientRepository;
    AuthorizationRepository authorizationRepository;
    AuthorizationConsentRepository authorizationConsentRepository;
    OidcClientRepository oidcClientRepository;

    @Autowired
    SecurityConfig(AuthorizationConsentRepository authorizationConsentRepository,
                   AuthorizationRepository authorizationRepository, ClientRepository clientRepository,
                   OidcClientRepository oidcClientRepository) {
        this.authorizationConsentRepository = authorizationConsentRepository;
        this.authorizationRepository = authorizationRepository;
        this.clientRepository = clientRepository;
        this.oidcClientRepository = oidcClientRepository;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        RegisteredClientRepository registeredClientRepository = new JpaRegisteredClientRepository(clientRepository);
        OAuth2AuthorizationService authorizationService = new JpaOAuth2AuthorizationService(authorizationRepository,
                registeredClientRepository);
        OAuth2AuthorizationConsentService authorizationConsentService =
                new JpaOAuth2AuthorizationConsentService(authorizationConsentRepository, registeredClientRepository);

        //@formatter:off
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        http.apply(authorizationServerConfigurer);

        authorizationServerConfigurer
                .registeredClientRepository(registeredClientRepository)
                .authorizationService(authorizationService)
                .authorizationConsentService(authorizationConsentService)
                .tokenGenerator(tokenGenerator())
                .clientAuthentication(clientAuthentication ->
                        clientAuthentication
                                .authenticationConverter(
                                        new PublicClientRefreshTokenAuthenticationConverter())
                                .authenticationProvider(
                                        new PublicClientRefreshTokenAuthenticationProvider(registeredClientRepository))
                )
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http.cors(Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
//                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/users/**", "/password/**").permitAll().anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher));

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, AuthenticationSuccessHandler sucessHandler) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/login", "/logout/connect/front-channel/sso-client", "/error", "/webjars/**", "/images/**", "/css/**", "/assets/**", "/favicon.ico", "/users/**", "/password/**", "/js/**").permitAll().anyRequest().authenticated())
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(form-> form.loginPage("/login").permitAll())
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(sucessHandler);
                    oauth2.loginPage("/login/oauth2").permitAll();
                }).logout(logout -> logout.logoutSuccessUrl("/login?logout=true")
                .logoutSuccessHandler(oidcLogoutSuccessHandler()))
                .addFilterBefore(staticResourceFilter(), AuthorizationFilter.class);


        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .securityMatchers(req -> req.requestMatchers("/users/**", "/password/**"))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(request -> StringUtils.hasText(request.getHeader("x-authentication")) && request.getHeader("x-authentication").equalsIgnoreCase("CUSTOM_AUTH")).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(resourceServer-> resourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter())));
        //@formatter:on

        return http.build();
    }

    Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
        return new CustomJwtAuthenticationConverter(new JwtGrantedAuthoritiesConverter(), JwtClaimNames.SUB);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(UserServiceOAuth2UserHandler handler) {
        SocialLoginAuthenticationSuccessHandler authenticationSuccessHandler =
                new SocialLoginAuthenticationSuccessHandler();
        authenticationSuccessHandler.setOidcUserHandler(handler);
        return authenticationSuccessHandler;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {

        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey =
                new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
        }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    OAuth2TokenGenerator<OAuth2Token> tokenGenerator() {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource()));
        jwtGenerator.setJwtCustomizer(jwtCustomizer());
        OAuth2TokenGenerator<OAuth2RefreshToken> refreshTokenGenerator = new PublicClientRefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, refreshTokenGenerator);
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            // customize jwt claims here
        };
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new JpaClientRegistrationRepository(oidcClientRepository);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository());

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return oidcLogoutSuccessHandler;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private Filter staticResourceFilter() {
        return (request, response, chain) -> {
            String path = ((HttpServletRequest) request).getRequestURI();
            boolean isSpa = path.startsWith(Constants.getUiBaseUri() + "/") || path.equalsIgnoreCase(Constants.getUiBaseUri());
            if (!isSpa) {
                chain.doFilter(request, response);
                return;
            }
            boolean isStaticResource = path.matches(".*\\.(js|ts|jsx|tsx|svg|png|css|ico|html)");

            if (isStaticResource) {
                chain.doFilter(request, response);
            } else {
                request.getRequestDispatcher(Constants.getUiBaseUri() + "/index.html").forward(request, response);
            }
        };
    }
}