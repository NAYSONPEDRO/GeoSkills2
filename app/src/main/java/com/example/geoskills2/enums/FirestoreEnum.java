package com.example.geoskills2.enums;

public enum FirestoreEnum {
    COLECTION_USERS("users"),
    NAME("name"),
    EMAIL("email"),
    POINTS("points"),
    PROFILE_SELECTED("profileSelected");

    private final String value;

    FirestoreEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
