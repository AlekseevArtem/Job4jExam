package ru.job4j.exam;

import java.util.List;

class Question {
    private int id;
    private String text;
    private List<Option> options;
    private int answer;

    Question(int id, String text, List<Option> options, int answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    String getText() {
        return this.text;
    }

    List getOptions() {
        return this.options;
    }

    int getAnswer() {
        return this.answer;
    }

    int getId() {
        return this.id;
    }
}