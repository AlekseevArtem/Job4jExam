package ru.job4j.exam.store;

public class ExamDbSchema {
    public static final class ExamTable {
        public static final String NAME = "exam";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String DESC = "desc";
            public static final String RESULT = "result";
            public static final String DATE = "date";
        }
    }

    public static final class QuestionTable {
        public static final String NAME = "question";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String EXAM_ID = "exam_id";
            public static final String ANSWER = "answer";
            public static final String CORRECT = "correct";
            public static final String POSITION = "position";
            public static final String HINT = "hint";
        }
    }

    public static final class OptionTable {
        public static final String NAME = "option";

        public static final class Cols {
            public static final String POSITION = "position";
            public static final String TEXT = "text";
            public static final String QUESTION_ID = "question_id";
        }
    }
}