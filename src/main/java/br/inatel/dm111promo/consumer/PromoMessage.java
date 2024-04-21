package br.inatel.dm111promo.consumer;

import java.util.List;

public record PromoMessage(
        String id,
        String name,
        String userId,
        String starting,
        String expiration,
        List<String>products,
        long lastUpdatedOn
) {
}
