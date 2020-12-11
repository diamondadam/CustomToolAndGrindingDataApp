package com.example.customtooldataapp.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class JobWithOperations {

    public JobWithOperations(){};

    @Embedded
    public Job job;

    @Relation(
            parentColumn = "jobId",
            entityColumn = "jobId",
            entity = Operation.class
    )
    public List<Operation> Operations;
    public Job getJob(){
        return this.job;
    }
}
