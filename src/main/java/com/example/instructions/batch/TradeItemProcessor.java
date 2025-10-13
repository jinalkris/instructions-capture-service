package com.example.instructions.batch;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InputTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.util.TradeTransformer;
import org.springframework.batch.item.ItemProcessor;

public class TradeItemProcessor implements ItemProcessor<InputTrade, PlatformTrade> {

    @Override
    public PlatformTrade process(InputTrade inputTrade) {

        CanonicalTrade canonicalTrade = TradeTransformer.toCanonicalTrade(inputTrade);
        System.out.println("Convert to canonical trade = " + canonicalTrade.toString());

        PlatformTrade platformTrade = TradeTransformer.toPlatformTrade(canonicalTrade);

        return platformTrade;
    }

}
