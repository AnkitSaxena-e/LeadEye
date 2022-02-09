package com.example.comleader.Modal;

public class Cus_Detail_Model {

    private String Cus_Name, Cus_Number, Cus_Plan, Cus_Address, Cus_Pin, Cus_Image, Cus_Month, Keyward,  Date, Time, PID, Who;

    public Cus_Detail_Model() {
    }

    public Cus_Detail_Model(String cus_Name, String cus_Number, String cus_Plan, String cus_Address, String cus_Pin, String cus_Image,
                            String cus_Month, String keyward, String date, String time, String PID, String who) {
        Cus_Name = cus_Name;
        Cus_Number = cus_Number;
        Cus_Plan = cus_Plan;
        Cus_Address = cus_Address;
        Cus_Pin = cus_Pin;
        Cus_Image = cus_Image;
        Cus_Month = cus_Month;
        Keyward = keyward;
        Date = date;
        Time = time;
        this.PID = PID;
        Who = who;
    }

    public String getKeyward() {
        return Keyward;
    }

    public void setKeyward(String keyward) {
        Keyward = keyward;
    }

    public String getCus_Name() {
        return Cus_Name;
    }

    public void setCus_Name(String cus_Name) {
        Cus_Name = cus_Name;
    }

    public String getCus_Number() {
        return Cus_Number;
    }

    public void setCus_Number(String cus_Number) {
        Cus_Number = cus_Number;
    }

    public String getCus_Plan() {
        return Cus_Plan;
    }

    public void setCus_Plan(String cus_Plan) {
        Cus_Plan = cus_Plan;
    }

    public String getCus_Address() {
        return Cus_Address;
    }

    public void setCus_Address(String cus_Address) {
        Cus_Address = cus_Address;
    }

    public String getCus_Pin() {
        return Cus_Pin;
    }

    public void setCus_Pin(String cus_Pin) {
        Cus_Pin = cus_Pin;
    }

    public String getCus_Image() {
        return Cus_Image;
    }

    public void setCus_Image(String cus_Image) {
        Cus_Image = cus_Image;
    }

    public String getCus_Month() {
        return Cus_Month;
    }

    public void setCus_Month(String cus_Month) {
        Cus_Month = cus_Month;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getWho() {
        return Who;
    }

    public void setWho(String who) {
        Who = who;
    }
}
