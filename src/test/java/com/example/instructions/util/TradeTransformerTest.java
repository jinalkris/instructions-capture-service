package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InputTrade;
import com.example.instructions.model.PlatformTrade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTransformerTest {

    @Test
    void testToCanonicalTrade() {
        // Arrange
        InputTrade inputTrade = new InputTrade("ACCT123", "acct1234",
                "abc123", "Buy",10000, "2025-10-02T00:00:00z");

        // Act
        CanonicalTrade canonical = TradeTransformer.toCanonicalTrade(inputTrade);

        // Assert
        assertEquals("ACCT123", canonical.platformId());
        assertEquals("****1234", canonical.account()); // masked
        assertEquals("ABC123", canonical.security());    // uppercased
        assertEquals("B", canonical.type());           // converted Buy -> B
        assertEquals(10000.0, canonical.amount());
        assertEquals("2025-10-02T00:00:00z", canonical.timestamp());
    }

    @Test
    void testMaskAccount() {
        assertEquals("****6789", TradeTransformer.maskAccount("123456789"));
        assertEquals("123", TradeTransformer.maskAccount("123")); // too short, no mask
        assertNull(TradeTransformer.maskAccount(null));
    }

    @Test
    void testConvertType() {
        assertEquals("B", TradeTransformer.convertType("Buy"));
        assertEquals("S", TradeTransformer.convertType("Sell"));
        assertEquals("S", TradeTransformer.convertType("SELL")); // case-insensitive
        assertEquals("", TradeTransformer.convertType(null));
    }

    @Test
    void testToPlatformTrade() {
        // Arrange
        CanonicalTrade canonical = new CanonicalTrade("ACCT123", "****1234",
                "ABC123", "B",10000, "2025-10-02T00:00:00z");

        // Act
        PlatformTrade platformTrade = TradeTransformer.toPlatformTrade(canonical);

        // Assert
        assertEquals("ACCT123", platformTrade.platform_id());
        assertEquals("****1234", platformTrade.trade().account());
        assertEquals("ABC123", platformTrade.trade().security());
        assertEquals("B", platformTrade.trade().type());
        assertEquals(10000.0, platformTrade.trade().amount());
        assertEquals("2025-10-02T00:00:00z", platformTrade.trade().timestamp());
    }
}