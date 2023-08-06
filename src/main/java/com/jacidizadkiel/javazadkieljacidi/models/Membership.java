package com.jacidizadkiel.javazadkieljacidi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
    
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;  


@Document(collection = "memberships")
public class Membership {

    @Id
    private String id;
    private String key;
    private String name;

    @Min(value = 0, message = "Minimum priority must be at least 0")
    @Max(value = 100, message = "Maximum priority can be 100")
    private int prio;
    private long duration;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrio() {
        return prio;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}