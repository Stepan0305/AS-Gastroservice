package com.timer.pdf.Models;

import java.util.ArrayList;

public class DataKeeper {
    String clientData, workDone, clientName;
    ArrayList<Part> parts;

    public DataKeeper(String clientData, String workDone, String clientName, ArrayList<Part> parts) {
        this.clientData = clientData;
        this.workDone = workDone;
        this.clientName = clientName;
        this.parts = parts;
    }

    public DataKeeper(String clientData, String workDone, String clientName) {
        this.clientData = clientData;
        this.workDone = workDone;
        this.clientName = clientName;
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
