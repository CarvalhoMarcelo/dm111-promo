package br.inatel.dm111promo.api.promo;

import br.inatel.dm111promo.persistence.promo.PromoProduct;

import java.util.List;

//{
//    "name": "Promocao Dia das Maes",
//    "starting": "15/04/2024",
//    "expiration": "15/04/2024",
//    "products": [
//        {
//        "productId": "id do produto",
//        "discount": 15
//        },
//        {
//        "productId": "id do produto",
//        "discount": 15
//        }
//    ]
//}
public record PromoRequest(String name, String starting, String expiration, List<PromoProduct> products) {
}
