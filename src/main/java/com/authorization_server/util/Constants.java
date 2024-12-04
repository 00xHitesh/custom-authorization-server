package com.authorization_server.util;

public class Constants {

    private static final String EMAIL_CLAIM = "preferred_username";
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String UI_BASE_URI = "/i";

    public static String getEmailClaim() {
        return EMAIL_CLAIM;
    }

    public static String getAuthoritiesClaim() {
        return AUTHORITIES_CLAIM;
    }

    public static String getUiBaseUri() {
        return UI_BASE_URI;
    }

}
