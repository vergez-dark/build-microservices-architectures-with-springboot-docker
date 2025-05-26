package com.jeffrey.POST_SERVICE.security.utils;

public class JWTUtil {

    public static final String SECRET = "monSecretSecret1234";
    public static final String AUTH_HEADER = "Authorization";
    public static final long EXPIRE_ACCESS_TOKEN = 20*60*1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long EXPIRE_REFRESH_TOKEN = 15*60*1000;
}
