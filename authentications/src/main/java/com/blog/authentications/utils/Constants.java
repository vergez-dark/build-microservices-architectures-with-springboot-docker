package com.blog.authentications.utils;

public class Constants {
    Constants() {
    }

    public static final short STATE_ACTIVATED = 0;
    public static final short STATE_ARCHIVE = 1;
    public static final short STATE_DELETED = 2;
    public static final short STATE_DEACTIVATED = 3;
    public static final short STATE_LOCKED = 4;
    public static final short STATE_BANNED = 5;

    // Codes d'erreurs
    public static final String USER_NON_AUTHENTICATED = "401";
    public static final String CONNECTION_TIMEOUT = "402";
    public static final String ERROR_PAGE_NOT_FOUND = "404";
    public static final String SERVER_ERROR = "500";
    public static final String SERVER_DENY_RESPONSE = "504";
    public static final String ITEM_NOT_FOUND = "403";
    public static final String ITEM_ALREADY_EXIST = "501";
    public static final String ITEM_ALREADY_DELETED = "502";
    public static final String ITEM_ALREADY_DEACTIVATED = "503";
    public static final String ITEM_IS_REQUIRED = "504";
    public static final String INVALID_INPUT = "506";
    public static final String FILE_NOT_READ = "507";
    public static final String FILE_NOT_DOWNLOAD = "508";
    public static final String MAIL_NOT_SEND = "509";
    public static final String NOT_ACTIVE_ITEM = "510";
    public static final String RUN_TIME = "511";
    public static final String NULL_POINTER = "512";

    // rôles par défaut
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    public static final String USER_ALREADY_DELETED_CODE = "600";
    public static final String USER_ALREADY_DELETED = "User already deleted";

    public static final String USER_ACCOUNT_ALREADY_LOCKED_CODE = "601";
    public static final String USER_ACCOUNT_ALREADY_LOCKED = "User Account already locked.";

    public static final String USER_ALREADY_DEACTIVATED = "User already deactivated";
    public static final String USER_ALREADY_DEACTIVATED_CODE = "602";

    public static final String USER_ALREADY_ARCHIVE = "User already archive";
    public static final String USER_ALREADY_ARCHIVE_CODE = "603";

    public static final String USER_ALREADY_BANNED = "User already banned";
    public static final String USER_ALREADY_BANNED_CODE = "604";

    public static final String ACCOUNT_ACTIVATION_LINK_NOT_FOUND_CODE = "605";
    public static final String ACCOUNT_ACTIVATION_LINK_NOT_FOUND = "Le lien d'activation de compte que vous avez fourni n'existe pas.";

    public static final String ACCOUNT_ACTIVATION_LINK_EXPIRED_CODE = "606";
    public static final String ACCOUNT_ACTIVATION_LINK_EXPIRED = "Le lien d'activation de votre compte est expiré; veuillez renvoyer de nouveau.";

    public static final String USER_NOT_FOUND_CODE = "611";
    public static final String USER_NOT_FOUND = "User not found";

    public static final String RESOURCE_UPDATE_UNAUTHORIZE_CODE = "612";
    public static final String RESOURCE_UPDATE_UNAUTHORIZE = "Vous n'etes pas autorise a mettre a jour cette resource";

    public static final String RESOURCE_NOT_FOUND_CODE = "613";

    public static String resourceNotFound(String className) {
        return className + " not found";
    }

}
