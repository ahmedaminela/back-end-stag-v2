package com.example.rh.dto;

public class FeedbackRequest {
    private Double notes;
    private String encadrantComments;

    // Getters and Setters

    public Double getNotes() {
        return notes;
    }

    public void setNotes(Double notes) {
        this.notes = notes;
    }

    public String getEncadrantComments() {
        return encadrantComments;
    }

    public void setEncadrantComments(String encadrantComments) {
        this.encadrantComments = encadrantComments;
    }
}

