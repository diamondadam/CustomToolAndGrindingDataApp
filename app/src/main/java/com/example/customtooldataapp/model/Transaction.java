package com.example.customtooldataapp.model;

import androidx.lifecycle.LiveData;

import java.util.Objects;

public class Transaction {
    // Key id is the same as the operation number for the operation...
    // Job id is created when transaction is
    //
    private String transactionPath;
    private String tranType;
    private String tranID;
    private String logout;
    private String keyID;
    private Employee employee;
    private String jobId = "94937";
    private String jobName = "SM-20201029";
    private String operationName = "PWR010-025";

    public Transaction(String path, String jobId) {
        this.transactionPath = path;
        this.jobId = jobId;
        this.employee = Employee.getInstance();
        parsePath(path);
    }

    private void parsePath(String path){

        this.tranType = path.split("&", 4)[0].replace("OpStop.aspx?tranType=", "");
        this.tranID = path.split("&", 4)[2].replace("tranID=", "");
        this.logout = path.split("&", 4)[3].replace("logOut=", "");
        this.keyID = path.split("&", 4)[1].replace("keyID=", "");
    }

    public Job getJob() {
        return employee.getJob(jobId);
    }

    public Operation getOperation() {
        return employee.getJob(jobId).getOperation(keyID);
    }

    public String getJobName() {
        return jobName;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getOperationId() {
        return keyID;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;

        if (otherObject == null) return false;

        if (!(otherObject instanceof com.example.customtooldataapp.model.Transaction)) return false;

        com.example.customtooldataapp.model.Transaction other = (com.example.customtooldataapp.model.Transaction) otherObject;
        return (tranType.equals(other.tranType)
                && tranID.equals(other.tranID)
                && logout.equals(other.logout)
                && keyID.equals(other.keyID)
                && transactionPath.equals(other.transactionPath));
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionPath, tranType, tranID, logout, keyID);
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTransactionPath() {
        return transactionPath;
    }

    public void setTransactionPath(String transactionPath) {
        this.transactionPath = transactionPath;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

}
