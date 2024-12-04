package com.authorization_server.controller;

import com.authorization_server.entity.SsoDomain;
import com.authorization_server.repository.JpaClientRegistrationRepository;
import com.authorization_server.repository.SsoDomainRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class ViewController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private SsoDomainRepository ssoDomainRepository;

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @GetMapping("/login")
    String login(Model model) {
        if (clientRegistrationRepository instanceof JpaClientRegistrationRepository clientRegistrations) {

            Map<String, String> clients = new HashMap<>();
            List<SsoDomain> ssoDomainList = ssoDomainRepository.findAll();

            for (ClientRegistration client : clientRegistrations) {
                clients.put(client.getRegistrationId(),
                        OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + client.getRegistrationId());
            }

            JSONArray jsonArray = new JSONArray();

            ssoDomainList.forEach(ssoDomain -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("domainName", ssoDomain.getDomainName());
                jsonObject.put("oidcRegistrationId", ssoDomain.getOidcRegistrationId());
                jsonObject.put("redirectUri", clients.get(ssoDomain.getOidcRegistrationId()));
                jsonArray.add(jsonObject);
            });
            model.addAttribute("ssoDomains", jsonArray.toJSONString());
        }
        return "login";
    }

    @GetMapping("/")
    String index(HttpServletRequest request, Model model) {
//        CustomUserDetails userDetails = ((CustomUserDetails) ((UsernamePasswordAuthenticationToken) request.getUserPrincipal()).getPrincipal());
//        StringBuilder name = new StringBuilder();
//
//        if (userDetails.getFirstName() != null)
//            name.append(userDetails.getFirstName()).append(" ");
//        if (userDetails.getLastName() != null)
//            name.append(userDetails.getLastName());
//        if (name.isEmpty())
//            name.append("User");
//
//        model.addAttribute("name", name.toString());

        return "redirect:/i/home";
    }

    @GetMapping("/logout")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        return "redirect:/login?logout=true";
    }
}
