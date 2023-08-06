package com.jacidizadkiel.javazadkieljacidi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;

    @Min(value = 0, message = "Minimum cost must be at least 0")
    private BigDecimal cost;

    @Min(value = 0, message = "Minimum priority must be at least 0")
    @Max(value = 100, message = "Maximum priority can be 100")
    private int minPrio;

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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getMinPrio() {
        return minPrio;
    }

    public void setMinPrio(int minPrio) {
        this.minPrio = minPrio;
    }
}