package com.customtoolandgrinding.customtooldataapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "punch_hole_table")
public class PunchHole {
    @Ignore
    private final int year = Calendar.getInstance().get(Calendar.YEAR);

    public PunchHole(){
        date = day;
    }

    public PunchHole(String fullString){
        this.date = stringToDate(fullString);
    }

    @PrimaryKey
    @NonNull
    private String date;
    private String day = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
    //True is punch in, False is punch out
    private Boolean prefix;

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

    public Boolean getPrefix() {
        return prefix;
    }

    public void setPrefix(Boolean prefix) {
        this.prefix = prefix;
    }
    @Ignore
    private String stringToDate(String date){
        date = date.replace("at", String.valueOf(year));
        if(date.contains("Clock In Time: ")){
            prefix = true;
            return date.replace("Clock In Time: ", "");
        }else{
            prefix = false;
            return date.replace("Clock Out Time: ", "");
        }

    }
}

