package ru.wear.store.web.models;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private List<Clothes> clothesList = new ArrayList<>();

    public void addClothes(Clothes clothes){
        clothesList.add(clothes);
    }

    public BigDecimal getTotalCoast(){
        BigDecimal totalCoast = new BigDecimal(0);
        for(Clothes clothes : clothesList){
            totalCoast = totalCoast.add(clothes.getCoast());
        }
        return totalCoast;
    }
}
