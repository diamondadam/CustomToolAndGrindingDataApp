package com.example.customtooldataapp.model;

import com.example.customtooldataapp.source.TransactionsRepository;
import com.example.customtooldataapp.source.remote.JobBossClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Employee {

    private static Employee instance;
    private String empId;
    private final HashMap<String, Job> jobs;

    public static Employee getInstance() {
        if (instance == null) {
            instance = new Employee("0163");
        }
        return instance;
    }

    private Employee() {
        this.jobs = new HashMap<>();
    }

    public Employee(String empId) {
        this.empId = empId;
        this.jobs = new HashMap<>();
    }


    public void addJob(Job job) {
        jobs.put(job.getJobId(), job);
    }

    public void deleteJob(Job job) {
        jobs.remove(job.getJobId());
    }

    public void updateJob(Job job) {
        jobs.put(job.getJobId(), job);
    }

    public void clearJobs(Job job) {
        jobs.clear();
    }

    public Job getJob(String jobId) {
        return jobs.get(jobId);
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

}
