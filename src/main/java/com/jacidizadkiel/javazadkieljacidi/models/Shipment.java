package com.jacidizadkiel.javazadkieljacidi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

@Document(collection = "shipments")
public class Shipment {

    @Id
    private String id;
    private String client;
    private List<Product> productList;

    @Min(value = 0, message = "Minimum totalCost must be at least 0")
    private BigDecimal totalCost;
    private Date deliverDate;


    private BigDecimal calculateTotalCost(List<Product> productList) {
        BigDecimal total = BigDecimal.ZERO;
        for (Product product : productList) {
            total = total.add(product.getCost());
        }
        return total;
    }


    private Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    private Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList, int priority) {

        for (Product product : productList) {
            
            if (priority > product.getMinPrio()) {
           
                throw new IllegalArgumentException("Product priority is too low for the client's membership");
            }
        }
        this.productList = productList;
        this.totalCost = calculateTotalCost(productList);
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }
    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Date calculateDeliverDate(int membershipPriority, List<Product> products) {
    
        Date calculatedDeliverDate = new Date();
        if (membershipPriority > 80) {
            int hoursToAdd = products.size();
            return addHoursToDate(calculatedDeliverDate, hoursToAdd);
        }  
        if (membershipPriority > 50) {
            int hoursToAdd = products.size() * 12;
            return addHoursToDate(calculatedDeliverDate, hoursToAdd);
        } 
        if (membershipPriority > 0) {
            int daysToAdd = products.size();
            return addDaysToDate(calculatedDeliverDate, daysToAdd);
        }
        return calculatedDeliverDate;
    }


    
}