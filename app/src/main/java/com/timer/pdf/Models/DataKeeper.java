package com.timer.pdf.Models;

import java.util.ArrayList;

public class DataKeeper {
    private String clientData, workDone, clientName, email;
    private ArrayList<Part> parts;

    public DataKeeper(String clientData, String workDone, String clientName, String email, ArrayList<Part> parts) {
        this.clientData = clientData;
        this.workDone = workDone;
        this.clientName = clientName;
        this.parts = parts;
        this.email = email;
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
