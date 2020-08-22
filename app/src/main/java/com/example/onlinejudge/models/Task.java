package com.example.onlinejudge.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "saved_tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected String name;
    protected String description;
    protected int memoryLimit;
    protected int timeLimit;
    protected Date timeSubmitted;
    protected String origin;
    @Ignore
    protected User user;
    @Ignore
    protected List<TestCase> testCases;
    @Ignore
    protected List<Tag> tags;

    public Task() {}

    @Ignore
    public Task(int id, String name, String description, int memoryLimit, int timeLimit, Date timeSubmitted, String origin) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.memoryLimit = memoryLimit;
        this.timeLimit = timeLimit;
        this.timeSubmitted = timeSubmitted;
        this.origin = origin;
    }

    @Ignore
    public Task(int id, String name, String description, int memoryLimit, int timeLimit, Date timeSubmitted, String origin, User user, List<TestCase> testCases, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.memoryLimit = memoryLimit;
        this.timeLimit = timeLimit;
        this.timeSubmitted = timeSubmitted;
        this.origin = origin;
        this.user = user;
        this.testCases = testCases;
        this.tags = tags;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Date getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(Date timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", memoryLimit=" + memoryLimit +
                ", timeLimit=" + timeLimit +
                ", timeSubmitted=" + timeSubmitted +
                ", origin='" + origin + '\'' +
                ", user=" + user +
                ", testCases=" + testCases +
                ", tags=" + tags +
                '}';
    }
}
