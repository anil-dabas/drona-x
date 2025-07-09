package com.spot.exchange.order.cache;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ListedPairsCache {

    public static Set<String> listedPairs;
    public void initCache(){
        listedPairs = new HashSet<>();
        listedPairs.add("BTC-USDT");
    }
}
