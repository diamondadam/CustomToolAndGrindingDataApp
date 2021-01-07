package com.example.customtooldataapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "clock_in_and_out_table")
public class ClockInAndOut {

    @Ignore
    private final int year = Calendar.getInstance().get(Calendar.YEAR);

    @PrimaryKey
    @NonNull
    private String date;

    private String day = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());

    public ClockInAndOut(){
        date = day;
    }

    public ClockInAndOut(String fullString){
        this.date = parseTime(fullString);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day){
        this.day = day;
    }

    @Ignore
    private String parseTime(String str){
        //Replace the "at" with year
        String time = str.replace("at", String.valueOf(year));
        return time;
    }
}

