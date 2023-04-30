package ru.wear.store.web.models;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Product {
    private long id;
    private String name;
    private String description;
    private byte[] image;
    private String size;
    private BigDecimal coast;
    private int discount;
    private boolean outOfStock;

    public Product(){
        coast = new BigDecimal(0);
        discount = 0;
    }

    public BigDecimal getCoast(){
        float percent = ((100.0f - (float)(Math.min(discount, 100))) / 100.0f);
        return coast.multiply(BigDecimal.valueOf(percent)).setScale(2, RoundingMode.HALF_UP);
    }
}
