package com.example.eadditivesbot.service;

public class Additive {
    private Integer id;
    private String code;
    private String name;
    private String engName;
    private String risk;
    private String description;
    public Additive(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
