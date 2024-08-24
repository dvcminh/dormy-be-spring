package com.minhvu.friend.model.enums;

public enum Status {


    PENDING,
    ACCEPTED,
    REJECTED;

    public static Status fromString(String status) {
        switch (status) {
            case "PENDING":
                return PENDING;
            case "ACCEPTED":
                return ACCEPTED;
            case "REJECTED":
                return REJECTED;
            default:
                throw new RuntimeException("Status not found");
        }
    }
}
