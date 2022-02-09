package com.example.comleader.Modal;

public class Order {

    private String TotalAmount, Name, PhoneNumber, Address, ByUser, City, Pin, Quantity, FromUser, Check, Buy,
            KeyWord, Return, ONO, PID, Date, Time, DeliveryDetail, PNO, FinalTime, BuyType;

    public Order() {
    }

    public Order(String totalAmount, String name, String phoneNumber, String address, String byUser, String city, String pin, String quantity, String fromUser,
                 String check, String buy, String keyWord, String aReturn, String ONO, String PID, String date, String time, String deliveryDetail, String pno, String finalTime, String buyType) {
        TotalAmount = totalAmount;
        Name = name;
        PhoneNumber = phoneNumber;
        Address = address;
        ByUser = byUser;
        City = city;
        Pin = pin;
        Quantity = quantity;
        FromUser = fromUser;
        Check = check;
        Buy = buy;
        KeyWord = keyWord;
        Return = aReturn;
        this.ONO = ONO;
        this.PID = PID;
        Date = date;
        Time = time;
        DeliveryDetail = deliveryDetail;
        PNO = pno;
        FinalTime = finalTime;
        BuyType = buyType;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getFromUser() {
        return FromUser;
    }

    public void setFromUser(String fromUser) {
        FromUser = fromUser;
    }

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }

    public String getBuy() {
        return Buy;
    }

    public void setBuy(String buy) {
        Buy = buy;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String keyWord) {
        KeyWord = keyWord;
    }

    public String getReturn() {
        return Return;
    }

    public void setReturn(String aReturn) {
        Return = aReturn;
    }

    public String getONO() {
        return ONO;
    }

    public void setONO(String ONO) {
        this.ONO = ONO;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
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

    public String getDeliveryDetail() {
        return DeliveryDetail;
    }

    public void setDeliveryDetail(String deliveryDetail) {
        DeliveryDetail = deliveryDetail;
    }

    public String getPNO() {
        return PNO;
    }

    public void setPNO(String PNO) {
        this.PNO = PNO;
    }

    public String getByUser() {
        return ByUser;
    }

    public void setByUser(String byUser) {
        ByUser = byUser;
    }

    public String getFinalTime() {
        return FinalTime;
    }

    public void setFinalTime(String finalTime) {
        FinalTime = finalTime;
    }

    public String getBuyType() {
        return BuyType;
    }

    public void setBuyType(String buyType) {
        BuyType = buyType;
    }
}
