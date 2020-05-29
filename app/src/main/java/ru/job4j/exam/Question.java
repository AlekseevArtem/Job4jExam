package ru.job4j.exam;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Option> options;
    private int answer;

    public Question(int id, String text, List<Option> options, int answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public String getText() {
        return this.text;
    }

    public List getOptions() {
        return this.options;
    }

    public int getAnswer() {
        return this.answer;
    }

    public int getId() {
        return this.id;
    }
}