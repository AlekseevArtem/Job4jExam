package ru.job4j.exam;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Option> options;
    private int answer;
    private int correct;
    private String hint;

    public Question(int id, String text, List<Option> options, int answer, int correct, String hint) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.correct = correct;
        this.hint = hint;
    }

    public String getText() {
        return this.text;
    }

    public List<Option> getOptions() {
        return this.options;
    }

    public int getAnswer() {
        return this.answer;
    }

    public int getId() {
        return this.id;
    }

    public int getCorrect() {
        return correct;
    }

    public String getHint() {
        return hint;
    }
}