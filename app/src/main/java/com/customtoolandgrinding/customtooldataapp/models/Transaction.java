package com.customtoolandgrinding.customtooldataapp.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.customtoolandgrinding.customtooldataapp.source.ConnectionError;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "transaction_table")
public class Transaction {

    @PrimaryKey
    @NonNull
    private String tranID = "";
    private String transactionPath = "";
    private String tranType = "";
    private String logout = "";
    private String keyID = "";
    private String errorMessage = "";

    @Embedded
    private Job job;

    @Embedded
    private Operation operation;

    public Transaction(){}

    public Transaction(String path) {
        this.transactionPath = path;
        parsePath(path);
    }

    @Ignore
    public Transaction(ConnectionError connectionError){
        this.errorMessage = connectionError.getMessage();
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    private void parsePath(String path) {
        //OpStop.aspx?tranType=10&keyID=217000&tranID=5AE-6778-SH234-0&logOut=Yes

        this.tranType = path.split("&", 4)[0].replace("OpStop.aspx?tranType=", "");
        Log.d("parsePath", tranType);
        this.keyID = path.split("&", 4)[1].replace("keyID=", "");
        Log.d("parsePath", keyID);
        this.tranID = path.split("&", 4)[2].replace("tranID=", "");
        Log.d("parsePath", tranID);
        this.logout = path.split("&", 4)[3].replace("logOut=", "");
        Log.d("parsePath", logout);

    }


    public String getOperationId() {
        return keyID;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;

        if (otherObject == null) return false;

        if (!(otherObject instanceof Transaction)) return false;

        Transaction other = (Transaction) otherObject;
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

    @NotNull
    public String getTranID() {
        return tranID;
    }

    public void setTranID(@NotNull String tranID) {
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

    @NonNull
    @Override
    public String toString() {
        return "Transaction\n\tTranId: " + tranID + "\n\tTranType: " + tranType + "\n\tLogout: " + logout + "\n\tkeyId: " + keyID
                + "\n\t\tJobId: " + getJob().getJobId() + "\n\t\tOperation Id: " + getOperation().getOperationNumber();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
