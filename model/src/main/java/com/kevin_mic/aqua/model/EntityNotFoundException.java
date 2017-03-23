package com.kevin_mic.aqua.model;

public class EntityNotFoundException extends RuntimeException {
    private String type;
    private int id;

    public EntityNotFoundException(String type, int id) {
        super("Type:" + type + " id:" + id);
        this.type = type;
        this.id = id;
    }
}
