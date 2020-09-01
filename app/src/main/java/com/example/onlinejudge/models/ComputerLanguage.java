package com.example.onlinejudge.models;

public class ComputerLanguage {
    protected int id;
    protected String name;
    protected String extension;

    public ComputerLanguage() {}

    public ComputerLanguage(int id, String name, String extension) {
        this.id = id;
        this.name = name;
        this.extension = extension;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return name;
    }
}
