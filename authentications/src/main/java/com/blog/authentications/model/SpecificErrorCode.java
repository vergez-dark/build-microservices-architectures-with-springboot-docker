package com.blog.authentications.model;

public enum SpecificErrorCode {
    NOT_FOUND(404), UNHAUTORIZED(403), INTERNAL_SERVER_ERROR(500), NULL_DATA(400), SUCCESS(201),
    USER_ALREADY_EXIST(401),
    NAME_ALREADY_TAKEN(600);

    private final int code;

    SpecificErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
