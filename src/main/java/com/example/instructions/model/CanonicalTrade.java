package com.example.instructions.model;

public record CanonicalTrade(String platformId, String account,
                             String security,
                             String type,
                             int amount,
                             String timestamp) {
}
