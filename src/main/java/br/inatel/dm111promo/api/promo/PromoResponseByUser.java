package br.inatel.dm111promo.api.promo;

import br.inatel.dm111promo.persistence.promo.PromoProduct;

import java.util.List;

//{
//    "id": "",
//    "name": "Promocao Dia das Maes",
//    "starting": "15/04/2024",
//    "expiration": "15/04/2024",
//    "productsForYou": [
//        {
//        "productId": "id do produto",
//        "discount": 99
//        }
//    "products": [
//        {
//        "productId": "id do produto",
//        "discount": 99
//        }
//    ]
//}
public class PromoResponseByUser {

    private String id;
    private String name;
    private String starting;
    private String expiration;
    private List<PromoProduct> productsForYou;
    private List<PromoProduct> products;

    public PromoResponseByUser() {
    }

    public PromoResponseByUser(String id, String name, String starting, String expiration, List<PromoProduct> productsForYou, List<PromoProduct> products) {
        this.id = id;
        this.name = name;
        this.starting = starting;
        this.expiration = expiration;
        this.productsForYou = productsForYou;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public List<PromoProduct> getProductsForYou() {
        return productsForYou;
    }

    public void setProductsForYou(List<PromoProduct> products) {
        this.productsForYou = products;
    }

    public List<PromoProduct> getProducts() {
        return products;
    }

    public void setProducts(List<PromoProduct> products) {
        this.products = products;
    }
}
