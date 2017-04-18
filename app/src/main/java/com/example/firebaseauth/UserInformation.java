package com.example.firebaseauth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jovian on 2/22/2017.
 */

public class UserInformation {

    public String name;
    public String address;
    public int angka;

    public UserInformation(){

    }

    public UserInformation(String name, String address, int angka) {
        this.name = name;
        this.address = address;
        this.angka = angka;
    }

    public void getUserInformation(String UID) throws IOException, JSONException {
        //System.out.println(UID);
        String urllink = "https://fir-auth-af652.firebaseio.com/" + UID + ".json";
        URL url;
        url = new URL(urllink);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStreamReader isr;
        isr = new InputStreamReader(conn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = in.readLine()) != null){
            sb.append(line).append("\n");
        }

        System.out.println(sb);
        String jsonstring = sb.toString();

        JSONObject obj = new JSONObject(jsonstring);

        this.name = obj.getString("name");
        System.out.println(this.name);
        this.address = obj.getString("address");
        this.angka = obj.getInt("angka");
    }

    public String getName(){
        return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    public int getAngka(){
        return this.angka;
    }
}
