package ru.wear.store.web.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class ClientOrder {
    private long id;
    private Client client;
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product){
        products.add(product);
    }

    public BigDecimal getTotalCoast(){
        BigDecimal totalCoast = new BigDecimal(0);
        for(Product product : products){
            totalCoast = totalCoast.add(product.getCoast());
        }
        return totalCoast;
    }
}
