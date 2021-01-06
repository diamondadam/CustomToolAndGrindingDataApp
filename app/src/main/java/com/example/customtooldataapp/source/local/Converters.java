package com.example.customtooldataapp.source.local;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public List<Date> toDateList(String genreIds) {
        List<Date> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(new Date(Long.parseLong(s)));
            }
        }
        return list;
    }

    @TypeConverter
    public String fromDateList(List<Date> list) {
        String genreIds = "";
        for (Date i : list) {
            genreIds += "," + i.getTime();
        }
        return genreIds;
    }
}
