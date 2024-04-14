package org.example.backend.models;

public interface Identifiable<T> {
    T getId();

    void setId(T id);
}
