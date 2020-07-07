package ru.job4j.exam.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamBaseHelper extends SQLiteOpenHelper {
    public static final String DB = "exams.db";
    public static final int VERSION = 2;
    public static final String HINT_FOR = "hint_for";
    public static final String EXAM_ID = "exam_id";

    public ExamBaseHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateStore(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        updateStore(db);
    }

    private String insertExam(String name, String desc, String result, String date) {
        return "insert into exam (name, desc, result, date) values (" +
                "'" + name + "'," +
                "'" + desc + "'," +
                "" + result + "," +
                "" + date + ")";
    }

    private void updateStore(SQLiteDatabase db) {
        db.execSQL(
                "create table " + ExamDbSchema.ExamTable.NAME + " (" +
                        "id integer primary key autoincrement, " +
                        ExamDbSchema.ExamTable.Cols.NAME + " text, " +
                        ExamDbSchema.ExamTable.Cols.DESC + " text, " +
                        ExamDbSchema.ExamTable.Cols.RESULT + " integer, " +
                        ExamDbSchema.ExamTable.Cols.DATE + " text)"
        );
        db.execSQL(insertExam("Java Base Exam", "Description for Java Base Exam", null, null));
        db.execSQL(insertExam("Some other", "Description for some other", null, null));
        db.execSQL(
                "create table " + ExamDbSchema.QuestionTable.NAME + " (" +
                        "id integer primary key autoincrement, " +
                        ExamDbSchema.QuestionTable.Cols.NAME + " text, " +
                        ExamDbSchema.QuestionTable.Cols.EXAM_ID + " integer, " +
                        ExamDbSchema.QuestionTable.Cols.ANSWER + " integer, " +
                        ExamDbSchema.QuestionTable.Cols.CORRECT + " integer, " +
                        ExamDbSchema.QuestionTable.Cols.POSITION + " integer, " +
                        ExamDbSchema.QuestionTable.Cols.HINT + " text)"
        );
        db.execSQL(insertQuestion("How many primitive variables does Java have?", 1, 0, 3, 1, "Java have eight primitive variables"));
        db.execSQL(insertQuestion("What is Java Virtual Machine?", 1, 0, 2, 2, "Just google \"JVM\")"));
        db.execSQL(insertQuestion("What is happen if we try unboxing null?", 1, 0, 4, 3, "Curse for the programmer, called \"NPE\""));
        db.execSQL(insertQuestion("First", 2, 0, 3, 1, "First hint"));
        db.execSQL(insertQuestion("Second", 2, 0, 2, 2, "Second hint"));
        db.execSQL(
                "create table " + ExamDbSchema.OptionTable.NAME + " (" +
                        "id integer primary key autoincrement, " +
                        ExamDbSchema.OptionTable.Cols.POSITION + " integer, " +
                        ExamDbSchema.OptionTable.Cols.TEXT + " text, " +
                        ExamDbSchema.OptionTable.Cols.QUESTION_ID + " integer)"
        );
        db.execSQL(insertOption(1, "6", 1));
        db.execSQL(insertOption(2, "7", 1));
        db.execSQL(insertOption(3, "8", 1));
        db.execSQL(insertOption(4, "9", 1));
        db.execSQL(insertOption(1, "JVM one", 2));
        db.execSQL(insertOption(2, "JVM one current answer", 2));
        db.execSQL(insertOption(3, "JVM one", 2));
        db.execSQL(insertOption(4, "JVM one", 2));
        db.execSQL(insertOption(1, "something unexpected", 3));
        db.execSQL(insertOption(2, "absolute destroy everything", 3));
        db.execSQL(insertOption(3, "nothing", 3));
        db.execSQL(insertOption(4, "NPE!!", 3));
        db.execSQL(insertOption(1, "1.1", 4));
        db.execSQL(insertOption(2, "1.2", 4));
        db.execSQL(insertOption(3, "1.3", 4));
        db.execSQL(insertOption(4, "1.4", 4));
        db.execSQL(insertOption(1, "2.1", 5));
        db.execSQL(insertOption(2, "2.2", 5));
        db.execSQL(insertOption(3, "2.3", 5));
        db.execSQL(insertOption(4, "2.4", 5));
    }

    private String insertQuestion(String name, int exam_id, int answer, int correct, int position, String hint) {
        return "insert into question (name, exam_id, answer, correct, position ,hint) values (" +
                "'" + name + "'," +
                exam_id + "," +
                answer + "," +
                "'" + correct + "'," +
                "'" + position + "'," +
                "'" + hint + "')";
    }

    private String insertOption(int position, String text, int question_id) {
        return "insert into option (position, text, question_id) values (" +
                "'" + position + "'," +
                "'" + text + "'," +
                "'" + question_id + "')";
    }
}