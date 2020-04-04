package com.example.covid_19_update;

import java.io.Serializable;


public class Question implements Serializable {

    String question;
    String point;
    boolean isSelected;

    public Question(String question, String point, boolean isSelected) {
        this.question = question;
        this.point = point;
        this.isSelected = isSelected;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
