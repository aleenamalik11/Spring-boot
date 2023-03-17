package com.hazelsoft.springsecurityjpa.dto;

public enum Status {
    ERROR(0),
    SUCCESS(1);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
