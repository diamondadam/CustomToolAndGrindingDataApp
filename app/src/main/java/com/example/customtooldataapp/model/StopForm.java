package com.example.customtooldataapp.model;

public class StopForm {
  private String operationId;
  private String notes;
  private boolean setupBool;
  private boolean completedBool;
  private boolean rework;
  private int quantityCompleted;
  private int scrapQuantity;
  private int scrapCode;
  private int reworkCode;
  private float laborHours;
  private String transactionPath;

  public String getTransactionPath() {
    return transactionPath;
  }

  public void setTransactionPath(String transactionPath) {
    this.transactionPath = transactionPath;
  }

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getSetupBool() {
    if(setupBool){
      return "S";
    }else {
      return "R";
    }
  }

  public void setSetupBool(boolean setupBool) {
    this.setupBool = setupBool;
  }

  public String getCompletedBool() {
    //TODO: Verify
    if(completedBool){
      return "on";
    }else{
      return "off";
    }
  }

  public void setCompletedBool(boolean completedBool) {
    this.completedBool = completedBool;
  }

  public String getRework() {
    //TODO: Verify
    if(rework){
      return "yes";
    }else{
      return "no";
    }
  }

  public void setRework(boolean rework) {
    this.rework = rework;
  }

  public int getQuantityCompleted() {
    return quantityCompleted;
  }

  public void setQuantityCompleted(int quantityCompleted) {
    this.quantityCompleted = quantityCompleted;
  }

  public int getScrapQuantity() {
    return scrapQuantity;
  }

  public void setScrapQuantity(int scrapQuantity) {
    this.scrapQuantity = scrapQuantity;
  }

  public int getScrapCode() {
    return scrapCode;
  }

  public void setScrapCode(int scrapCode) {
    this.scrapCode = scrapCode;
  }

  public int getReworkCode() {
    return reworkCode;
  }

  public void setReworkCode(int reworkCode) {
    this.reworkCode = reworkCode;
  }

  public float getLaborHours() {
    return laborHours;
  }

  public void setLaborHours(float laborHours) {
    this.laborHours = laborHours;
  }
}
