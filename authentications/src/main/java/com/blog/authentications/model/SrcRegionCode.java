package com.blog.authentications.model;

public enum SrcRegionCode {
    AUTH(001), CONTROLLER(002), SERVICE(003), REPOSITORY(004), BEANS(005),;

    private final int code;

    SrcRegionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
