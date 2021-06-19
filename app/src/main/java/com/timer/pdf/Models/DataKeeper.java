package com.timer.pdf.Models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DataKeeper {
    private String clientData, workDone, clientName, email, timePlace, orderNum, ourData = "Skolin, Aleksandr & Steinbock, Andrej GbR\n" +
            "\n" +
            "Wüstenhöferstrasse 191\n" +
            "45355 Essen";
    private ArrayList<Part> parts;
    private Bitmap clientSignature, ourSignature;
    private boolean finished;

    public DataKeeper(String clientData, String workDone, String clientName, String email,
                      ArrayList<Part> parts, boolean finished, String ourData, String timePlace, String orderNum) {
        this.clientData = clientData;
        this.workDone = workDone;
        this.clientName = clientName;
        this.parts = parts;
        this.email = email;
        this.finished = finished;
        this.ourData = ourData;
        this.timePlace = timePlace;
        this.orderNum = orderNum;
    }
    public DataKeeper(){

    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTimePlace() {
        return timePlace;
    }

    public void setTimePlace(String timePlace) {
        this.timePlace = timePlace;
    }

    public String getOurData() {
        return ourData;
    }

    public void setOurData(String ourData) {
        this.ourData = ourData;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Bitmap getClientSignature() {
        return clientSignature;
    }

    public void setClientSignature(Bitmap clientSignature) {
        this.clientSignature = clientSignature;
    }

    public Bitmap getOurSignature() {
        return ourSignature;
    }

    public void setOurSignature(Bitmap ourSignature) {
        this.ourSignature = ourSignature;
    }

    public DataKeeper(String clientData, String workDone, String clientName, String email, ArrayList<Part> parts, Bitmap clientSignature, Bitmap ourSignature) {
        this.clientData = clientData;
        this.workDone = workDone;
        this.clientName = clientName;
        this.email = email;
        this.parts = parts;
        this.clientSignature = clientSignature;
        this.ourSignature = ourSignature;
    }

    public DataKeeper(String clientData, String workDone, String clientName, String email) {
        this.clientData = clientData;
        this.workDone = workDone;
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientData() {
        return clientData;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }

    public String getWorkDone() {
        return workDone;
    }

    public void setWorkDone(String workDone) {
        this.workDone = workDone;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Part> parts) {
        this.parts = parts;
    }
}
