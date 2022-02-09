package com.example.comleader.Modal;

public class OrderDetailLayout {

    private String Date, Discount, Pdescription, Pname, Pimage, Pprice, Quantity, Time, Pid, Detail, SnR,  Info, Color, OrderNo;

    public OrderDetailLayout() {
    }

    public OrderDetailLayout(String date, String discount, String pdescription, String pname, String pimage, String pprice, String quantity, String time, String pid, String detail, String snR, String info, String color, String orderNo) {
        Date = date;
        Discount = discount;
        Pdescription = pdescription;
        Pname = pname;
        Pimage = pimage;
        Pprice = pprice;
        Quantity = quantity;
        Time = time;
        Pid = pid;
        Detail = detail;
        SnR = snR;
        Info = info;
        Color = color;
        OrderNo = orderNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPdescription() {
        return Pdescription;
    }

    public void setPdescription(String pdescription) {
        Pdescription = pdescription;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPimage() {
        return Pimage;
    }

    public void setPimage(String pimage) {
        Pimage = pimage;
    }

    public String getPprice() {
        return Pprice;
    }

    public void setPprice(String pprice) {
        Pprice = pprice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getSnR() {
        return SnR;
    }

    public void setSnR(String snR) {
        SnR = snR;
    }
}
