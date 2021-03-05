package com.gnardada.boot.domain;

public class Partner {
    private String name;

    public Partner() {
    }

    public Partner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "name='" + name + '\'' +
                '}';
    }
}
