package com.example.customtooldataapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Job {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private String jobId;
    // Job Data tab
    private Date orderDate;
    private Date nextDeliveryDate;
    private String jobName;
    private String description;
    private String drawing;
    private String revision;
    // Quantities Tab
    private int orderQty;
    private int makeQty;
    private int pickQty;
    private int inProductionQty;
    private int completedQty;
    private int shippedQty;
    // Customer Data
    private String customer;
    private String purchaseOrder;
    private String poLine;
    private String shipTo;
    private String address;
    // Routings Data
    private HashMap<String, Operation> operations = new HashMap<>();
    // Pick or Buy
    private String materialRequisition;
    private String material;
    private String pickDescription;
    private int qtyRequired;
    private int qtyPicked;
    private String pickStatus;
    private String notes;

    public Operation getOperation(String operationId) {
        return operations.get(operationId);
    }

    public void addOperation(Operation operation){
        operations.put(operation.getOperationNumber(), operation);
    }

    public Job(String jobId) {
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getOrderDate() {
        return dateFormat.format(orderDate);
    }

    public void setOrderDate(String orderDate) {
        try {
            this.orderDate = dateFormat.parse(orderDate);
        } catch (ParseException e) {
            this.orderDate = null;
        }
    }

    public String getNextDeliveryDate() {
        return dateFormat.format(nextDeliveryDate);
    }

    public void setNextDeliveryDate(String nextDeliveryDate) {
        try {
            this.nextDeliveryDate = dateFormat.parse(nextDeliveryDate);
        } catch (ParseException e) {
            this.orderDate = null;
            //TODO log
        }
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String partNumber) {
        this.jobName = partNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public int getMakeQty() {
        return makeQty;
    }

    public void setMakeQty(int makeQty) {
        this.makeQty = makeQty;
    }

    public int getPickQty() {
        return pickQty;
    }

    public void setPickQty(int pickQty) {
        this.pickQty = pickQty;
    }

    public int getInProductionQty() {
        return inProductionQty;
    }

    public void setInProductionQty(int inProductionQty) {
        this.inProductionQty = inProductionQty;
    }

    public int getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(int completedQty) {
        this.completedQty = completedQty;
    }

    public int getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(int shippedQty) {
        this.shippedQty = shippedQty;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getPoLine() {
        return poLine;
    }

    public void setPoLine(String poLine) {
        this.poLine = poLine;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getMaterialRequisition() {
        return materialRequisition;
    }

    public void setMaterialRequisition(String materialRequisition) {
        this.materialRequisition = materialRequisition;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPickDescription() {
        return pickDescription;
    }

    public void setPickDescription(String pickDescription) {
        this.pickDescription = pickDescription;
    }

    public int getQtyRequired() {
        return qtyRequired;
    }

    public void setQtyRequired(int qtyRequired) {
        this.qtyRequired = qtyRequired;
    }

    public int getQtyPicked() {
        return qtyPicked;
    }

    public void setQtyPicked(int qtyPicked) {
        this.qtyPicked = qtyPicked;
    }

    public String getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(String pickStatus) {
        this.pickStatus = pickStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
