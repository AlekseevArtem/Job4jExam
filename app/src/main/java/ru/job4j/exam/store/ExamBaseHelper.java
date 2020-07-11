package ru.job4j.exam.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.job4j.exam.Exam;
import ru.job4j.exam.Option;
import ru.job4j.exam.Question;

public class ExamBaseHelper extends SQLiteOpenHelper {
    public static final String DB = "exams.db";
    public static final int VERSION = 2;
    public static final String HINT_FOR = "hint_for";
    public static final String EXAM_ID = "exam_id";
    private static ExamBaseHelper INST;


    private ExamBaseHelper(Context context) {
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

    public static ExamBaseHelper getInstance(Context context) {
        if (INST == null) {
            INST = new ExamBaseHelper(context);
        }
        return INST;
    }

    public void addAnswerToTheQuestion(int answer, int examID, int position) {
        ContentValues value = new ContentValues();
        value.put(ExamDbSchema.QuestionTable.Cols.ANSWER, answer);
        this.getWritableDatabase().update(ExamDbSchema.QuestionTable.NAME,
                value,
                ExamDbSchema.QuestionTable.Cols.POSITION + " = ?" + " and " +
                        ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(position), String.valueOf(examID)});
    }

    public void setResultOfTheExam(int examID, int sumCurrent, int sumAll) {
        ContentValues value = new ContentValues();
        value.put(ExamDbSchema.ExamTable.Cols.DATE,
                new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis())));
        value.put(ExamDbSchema.ExamTable.Cols.RESULT,
                Math.round((double) sumCurrent / sumAll * Math.pow(10, 2)) / Math.pow(10, 2) * 100);
        this.getWritableDatabase().update(ExamDbSchema.ExamTable.NAME,
                value,
                "id = ?",
                new String[]{String.valueOf(examID)});
    }

    public List<Exam> getExams() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().query(
                ExamDbSchema.ExamTable.NAME,
                null, null, null,
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            exams.add(new Exam(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.NAME)),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return exams;
    }

    public List<Question> getQuestions(int examID) {
        List<Question> questions = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().query(
                ExamDbSchema.QuestionTable.NAME,
                null,
                ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(examID)},
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            questions.add(new Question(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)),
                    getOptions(cursor.getInt(cursor.getColumnIndex("id"))),
                    cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.ANSWER)),
                    cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.CORRECT)),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.HINT))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return questions;
    }

    private List<Option> getOptions(int questionID) {
        List<Option> options = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().query(
                ExamDbSchema.OptionTable.NAME,
                null,
                ExamDbSchema.OptionTable.Cols.QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionID)},
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            options.add(new Option(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.OptionTable.Cols.TEXT))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return options;
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

    private String insertExam(String name, String desc, String result, String date) {
        return "insert into exam (name, desc, result, date) values (" +
                "'" + name + "'," +
                "'" + desc + "'," +
                "" + result + "," +
                "" + date + ")";
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