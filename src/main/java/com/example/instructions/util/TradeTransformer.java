package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InputTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.model.Trade;

public class TradeTransformer {
    public static CanonicalTrade toCanonicalTrade(InputTrade trade) {
        CanonicalTrade canonicalTrade = new CanonicalTrade(trade.platformid(),
                maskAccount(trade.acctnumber()),
                trade.secid().toUpperCase(),
                convertType(trade.type()),
                trade.amount(), trade.time());
        return  canonicalTrade;
    }

    public static String maskAccount(String account) {
        if (account == null || account.length() < 4) return account;
        return "****" + account.substring(account.length() - 4);
    }

    public static String convertType(String type) {
        if (type == null)  return "";
        return type.equalsIgnoreCase("Buy") ? "B" : "S";
    }

    public static PlatformTrade toPlatformTrade(CanonicalTrade canonicalTrade) {
        Trade trade = new Trade(canonicalTrade.account(), canonicalTrade.security(),
                canonicalTrade.type(), canonicalTrade.amount(), canonicalTrade.timestamp());
        PlatformTrade platformTrade = new PlatformTrade(canonicalTrade.platformId(),trade);

        return  platformTrade;
    }
}
