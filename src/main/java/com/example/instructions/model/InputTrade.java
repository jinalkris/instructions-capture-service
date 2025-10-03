package com.example.instructions.model;

public record InputTrade(String platformid, String acctnumber,
                         String secid,
                         String type,
                         int amount,
                         String time) {

    @Override
    public String toString() {
        return String.format("%s %s %s %s", platformid, type, amount, time);
    }
}
