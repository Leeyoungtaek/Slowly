package com.naxesa.slowly;

import java.io.Serializable;

/**
 * Created by Lee young teak on 2016-09-03.
 */
public class Message implements Serializable {

    // Data
    private String place, person, content, Uid;

    // Constructor
    public Message(){
    }

    // Getter and Setter
    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
