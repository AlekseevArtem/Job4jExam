package ru.job4j.exam.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.job4j.exam.Option;
import ru.job4j.exam.Question;

public final class QuestionStore {
    private List<Question> questions = new ArrayList<>();
    private int position = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();
    public static final String HINT_FOR = "hint_for";
    private static final QuestionStore INST = new QuestionStore();
    private static final Map<Integer, String> answers = new HashMap<Integer, String>(){
        {
            put(0, "Hint 1");
            put(1, "Hint 2");
            put(2, "Hint 3");
        }
    };

    private QuestionStore() {
        questions.add(new Question(
                1, "How many primitive variables does Java have?",
                Arrays.asList(
                        new Option(1, "1.1"), new Option(2, "1.2"),
                        new Option(3, "1.3"), new Option(4, "1.4")
                ), 4
        ));
        questions.add(new Question(
                2, "What is Java Virtual Machine?",
                Arrays.asList(
                        new Option(1, "2.1"), new Option(2, "2.2"),
                        new Option(3, "2.3"), new Option(4, "2.4")
                ), 4
        ));
        questions.add(new Question(
                3, "What is happen if we try unboxing null?",
                Arrays.asList(
                        new Option(1, "3.1"), new Option(2, "3.2"),
                        new Option(3, "3.3"), new Option(4, "3.4")
                ), 4
        ));
    }

    public static QuestionStore getInstance() {
        return INST;
    }

    public void add(Question question) {
        questions.add(question);
    }

    public Question get(int position) {
        return questions.get(position);
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public int size() {
        return questions.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Map<Integer, Integer> getUserAnswers() {
        return userAnswers;
    }
}