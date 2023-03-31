package com.luffy.web.rest.errors;

public class EntityNotFoundException extends RuntimeException {
    private final String entity;

    public EntityNotFoundException(String entity) {
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return entity + "Not Found";
    }
}
