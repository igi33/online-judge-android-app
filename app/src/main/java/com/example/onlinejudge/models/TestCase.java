package com.example.onlinejudge.models;

public class TestCase {
    protected int id;
    protected String input;
    protected String output;

    public TestCase() {}

    public TestCase(int id, String input, String output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "id=" + id +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
