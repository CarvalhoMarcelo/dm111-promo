package br.inatel.dm111promo.persistence.promo;


public class PromoProduct {
    private String id;
    private long discount;

    public PromoProduct() {
    }

    public PromoProduct(String id, long discount) {
        this.id = id;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }
}
