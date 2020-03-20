package org.luizleiteoliveira.entity;

public class User {

    private String name;
    private String fullName;
    private String doc;
    private String email;

    public User(String name, String fullName, String doc, String email) {
        this.name = name;
        this.fullName = fullName;
        this.doc = doc;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
