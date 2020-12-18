package com.example.customtooldataapp.data.model;

public class Operation {

    private String operationNumber;
    private String wcVendor;
    private String opName;
    private String operationDescription;
    private String scheduledStart;
    private String status;
    private String runMethod;
    private String floorNotes;
    private int qtyCompleted;
    private float remainingRuntime;
    private float remainingSetupTime;
    private float setupTime;
    private float runtime;

    public Operation(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getWcVendor() {
        return wcVendor;
    }

    public void setWcVendor(String wcVendor) {
        this.wcVendor = wcVendor;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(String scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRunMethod() {
        return runMethod;
    }

    public void setRunMethod(String runMethod) {
        this.runMethod = runMethod;
    }

    public String getFloorNotes() {
        return floorNotes;
    }

    public void setFloorNotes(String floorNotes) {
        this.floorNotes = floorNotes;
    }

    public int getQtyCompleted() {
        return qtyCompleted;
    }

    public void setQtyCompleted(int qtyCompleted) {
        this.qtyCompleted = qtyCompleted;
    }

    public float getRemainingRuntime() {
        return remainingRuntime;
    }

    public void setRemainingRuntime(float remainingRuntime) {
        this.remainingRuntime = remainingRuntime;
    }

    public float getRemainingSetupTime() {
        return remainingSetupTime;
    }

    public void setRemainingSetupTime(float remainingSetupTime) {
        this.remainingSetupTime = remainingSetupTime;
    }

    public float getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(float setupTime) {
        this.setupTime = setupTime;
    }

    public float getRuntime() {
        return runtime;
    }

    public void setRuntime(float runtime) {
        this.runtime = runtime;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }
}
