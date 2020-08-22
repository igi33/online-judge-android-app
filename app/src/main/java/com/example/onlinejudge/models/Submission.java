package com.example.onlinejudge.models;

import java.util.Date;

public class Submission {
    protected int id;
    protected Date timeSubmitted;
    protected String sourceCode;
    protected String status;
    protected String message;
    protected int executionTime;
    protected int executionMemory;
    protected User user;
    protected Task task;
    protected ComputerLanguage computerLanguage;
    protected boolean selected;

    public Submission() {}

    public Submission(int id, Date timeSubmitted, String sourceCode, String status, String message, int executionTime, int executionMemory, User user, Task task, ComputerLanguage computerLanguage, boolean selected) {
        this.id = id;
        this.timeSubmitted = timeSubmitted;
        this.sourceCode = sourceCode;
        this.status = status;
        this.message = message;
        this.executionTime = executionTime;
        this.executionMemory = executionMemory;
        this.user = user;
        this.task = task;
        this.computerLanguage = computerLanguage;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(Date timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getExecutionMemory() {
        return executionMemory;
    }

    public void setExecutionMemory(int executionMemory) {
        this.executionMemory = executionMemory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ComputerLanguage getComputerLanguage() {
        return computerLanguage;
    }

    public void setComputerLanguage(ComputerLanguage computerLanguage) {
        this.computerLanguage = computerLanguage;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", timeSubmitted=" + timeSubmitted +
                ", sourceCode='" + sourceCode + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", executionTime=" + executionTime +
                ", executionMemory=" + executionMemory +
                ", user=" + user +
                ", task=" + task +
                ", computerLanguage=" + computerLanguage +
                ", selected=" + selected +
                '}';
    }
}
