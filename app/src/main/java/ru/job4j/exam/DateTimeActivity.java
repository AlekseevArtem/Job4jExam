package ru.job4j.exam;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DateTimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private String date;

    @Override
    protected void onCreate(@Nullable Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.datetime_activity);
        Button dateTime = findViewById(R.id.dateTimeSet);
        dateTime.setOnClickListener(view -> {
            DialogFragment dialog = new DatePickerFragment();
            dialog.show(Objects.requireNonNull(getSupportFragmentManager()), "date_tag");
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "." + month + "." + year;
        DialogFragment dialog = new TimePickerFragment();
        dialog.show(Objects.requireNonNull(getSupportFragmentManager()), "time_tag");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView result = findViewById(R.id.dateTimeResult);
        String time = hourOfDay + ":" + minute;
        result.setText(String.format("%s %s", date, time));
    }
}
