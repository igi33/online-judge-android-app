package com.example.onlinejudge.models;

public class SubmissionSkeleton {
    protected int langId;
    protected String sourceCode;

    public SubmissionSkeleton() {
    }

    public SubmissionSkeleton(int langId, String sourceCode) {
        this.langId = langId;
        this.sourceCode = sourceCode;
    }

    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
