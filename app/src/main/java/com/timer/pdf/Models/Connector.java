package com.timer.pdf.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Connector {
    public static final String PATH = "https://pdf-sender-app.herokuapp.com/Maker";

    public static void send (DataKeeper keeper){
        try {
            URL url = new URL(PATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientData", keeper.getClientData());
            jsonObject.put("clientName", keeper.getClientName());
            jsonObject.put("email", keeper.getEmail());
            jsonObject.put("workDone", keeper.getWorkDone());
            JSONArray array = new JSONArray();
            ArrayList<Part> parts = keeper.getParts();
            for (Part p: parts){
                JSONObject object = new JSONObject();
                object.put("name", p.getName());
                object.put("number", p.getNumber());
                object.put("count", p.getCount());
                array.put(p);
            }
            jsonObject.put("parts", array);
        } catch (Exception ex){}
    }
}
