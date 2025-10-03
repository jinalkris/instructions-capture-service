package com.example.instructions.model;

public record Trade(String account,
        String security,
        String type,
        int amount,
                    String timestamp) {

}

