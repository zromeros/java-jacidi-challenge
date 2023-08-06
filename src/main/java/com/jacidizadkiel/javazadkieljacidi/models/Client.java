package com.jacidizadkiel.javazadkieljacidi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "clients")
public class Client {

    @Id
    private String id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private Date lastDelivery;
    private Date nextRenewal;
    private String membership;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastDelivery() {
        return lastDelivery;
    }

    public void setLastDelivery(Date lastDelivery) {
        this.lastDelivery = lastDelivery;
    }

    public Date getNextRenewal() {
        return nextRenewal;
    }

    public void setNextRenewal(Date nextRenewal) {
        this.nextRenewal = nextRenewal;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }
}