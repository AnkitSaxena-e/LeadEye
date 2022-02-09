package com.example.comleader.Modal;

public class MemModel {

    private String Address, Image, Last_Seen, AppDate, Latitude, Longitude, Keyward, Name, Number, OnCall, Online, Passward, Suspend, Token;

    public MemModel() {
    }

    public MemModel(String address, String image, String last_Seen, String appDate, String latitude, String longitude, String keyward,
                    String name, String number, String onCall, String online, String passward, String suspend, String token) {
        Address = address;
        Image = image;
        Last_Seen = last_Seen;
        AppDate = appDate;
        Latitude = latitude;
        Longitude = longitude;
        Keyward = keyward;
        Name = name;
        Number = number;
        OnCall = onCall;
        Online = online;
        Passward = passward;
        Suspend = suspend;
        Token = token;
    }

    public String getKeyward() {
        return Keyward;
    }

    public void setKeyward(String keyward) {
        Keyward = keyward;
    }

    public String getAppDate() {
        return AppDate;
    }

    public void setAppDate(String appDate) {
        AppDate = appDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLast_Seen() {
        return Last_Seen;
    }

    public void setLast_Seen(String last_Seen) {
        Last_Seen = last_Seen;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getOnCall() {
        return OnCall;
    }

    public void setOnCall(String onCall) {
        OnCall = onCall;
    }

    public String getOnline() {
        return Online;
    }

    public void setOnline(String online) {
        Online = online;
    }

    public String getPassward() {
        return Passward;
    }

    public void setPassward(String passward) {
        Passward = passward;
    }

    public String getSuspend() {
        return Suspend;
    }

    public void setSuspend(String suspend) {
        Suspend = suspend;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
