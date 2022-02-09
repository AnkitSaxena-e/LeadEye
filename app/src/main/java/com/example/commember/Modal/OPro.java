package com.example.commember.Modal;

public class OPro {

    private String PName, PNum, PCom, PPri, PQut, PImage, PPid;

    public OPro() {
    }

    public OPro(String PName, String PNum, String PCom, String PPri, String pQut, String pImage, String pPid) {
        this.PName = PName;
        this.PNum = PNum;
        this.PCom = PCom;
        this.PPri = PPri;
        PQut = pQut;
        PImage = pImage;
        PPid = pPid;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getPNum() {
        return PNum;
    }

    public void setPNum(String PNum) {
        this.PNum = PNum;
    }

    public String getPCom() {
        return PCom;
    }

    public void setPCom(String PCom) {
        this.PCom = PCom;
    }

    public String getPPri() {
        return PPri;
    }

    public void setPPri(String PPri) {
        this.PPri = PPri;
    }

    public String getPQut() {
        return PQut;
    }

    public void setPQut(String PQut) {
        this.PQut = PQut;
    }

    public String getPImage() {
        return PImage;
    }

    public void setPImage(String PImage) {
        this.PImage = PImage;
    }

    public String getPPid() {
        return PPid;
    }

    public void setPPid(String PPid) {
        this.PPid = PPid;
    }
}
