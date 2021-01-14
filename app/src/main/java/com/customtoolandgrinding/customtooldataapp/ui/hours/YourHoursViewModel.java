package com.customtoolandgrinding.customtooldataapp.ui.hours;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customtoolandgrinding.customtooldataapp.models.PunchHole;
import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;
import com.customtoolandgrinding.customtooldataapp.source.local.PunchHoleDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class YourHoursViewModel extends ViewModel {
    private final TransactionRepository transactionRepository;
    private final MutableLiveData<HashMap<Integer, Long>> weeklyHours = new MutableLiveData<>();

    public YourHoursViewModel(Application application) {
        this.transactionRepository = TransactionRepository.getInstance(application);
        processWeeklyHours();
    }

    public LiveData<HashMap<Integer, Long>> getWeeklyHours(){
        processWeeklyHours();
        return weeklyHours;
    }

    private void processWeeklyHours() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        HashMap<Integer, Long> weeklyHoursHashMap = initializeHashMap();
        Calendar cal = Calendar.getInstance();

        PunchHoleDatabase.databaseWriteExecutor.execute(() -> {
            while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                try{
                    processPunchCard(weeklyHoursHashMap, transactionRepository.getPunchCardByDay(sdf.format(cal.getTime())), cal.get(Calendar.DAY_OF_WEEK));
                }catch(ParseException e){
                    Log.d("Parse Exception", e.getMessage());
                }
                //Go to the previous day
                cal.add(Calendar.DATE, -1);
            }
            weeklyHours.postValue(weeklyHoursHashMap);
        });
        weeklyHours.postValue(weeklyHoursHashMap);
    }

    private void processPunchCard(HashMap<Integer, Long> weeklyHoursHashMap, List<PunchHole> punchCards, Integer dayOfTheWeek) throws ParseException {
        PunchHole previousPunchHole = null;

        for(PunchHole punchHole: punchCards){
            if(punchHole.getDate().contains("Clock In Time: ")){
                previousPunchHole = punchHole;
            } else if(previousPunchHole != null){
                addData(weeklyHoursHashMap, previousPunchHole, punchHole, dayOfTheWeek);
                //Reset Previous
                previousPunchHole = null;
            }
        }
    }

    private void addData(HashMap<Integer, Long> weeklyHoursHashMap, PunchHole in, PunchHole out, Integer dayOfTheWeek) throws ParseException {
        SimpleDateFormat punchHoleFormat = new SimpleDateFormat("MMM d yyyy HH:mm a", Locale.US);
        Long totalTimeForTheDay = weeklyHoursHashMap.get(dayOfTheWeek) + punchHoleFormat.parse(out.getDate()).getTime() - punchHoleFormat.parse(in.getDate()).getTime();
        weeklyHoursHashMap.put(dayOfTheWeek, totalTimeForTheDay);

    }

    private HashMap<Integer, Long> initializeHashMap(){
        HashMap<Integer, Long> map = new HashMap<>();
        map.put(Calendar.SUNDAY, 0L);
        map.put(Calendar.MONDAY, 0L);
        map.put(Calendar.TUESDAY, 0L);
        map.put(Calendar.WEDNESDAY, 0L);
        map.put(Calendar.THURSDAY, 0L);
        map.put(Calendar.FRIDAY, 0L);
        map.put(Calendar.SATURDAY, 0L);
        return map;
    }

}
