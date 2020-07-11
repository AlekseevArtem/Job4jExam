package ru.job4j.exam;

import androidx.annotation.NonNull;

public class Exam {
    private int id;
    private String name;
    private String desc;
    private String time;
    private String result;

    public Exam(int id, String name, String desc, String time, String result) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDesc() {
        return desc;
    }

    public String getResult() {
        return result;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id == exam.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", result=" + result +
                '}';
    }
}
