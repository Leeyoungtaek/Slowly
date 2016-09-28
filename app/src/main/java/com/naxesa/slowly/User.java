package com.naxesa.slowly;

/**
 * Created by Lee young teak on 2016-09-13.
 */
public class User {

    // Data
    private String name, email;

    User(){}

    User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
