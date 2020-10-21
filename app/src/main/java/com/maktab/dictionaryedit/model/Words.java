package com.maktab.dictionaryedit.model;

public class Words {
    //private int id;
    private String Enword;
    private String Faword;


    public Words(String enword, String faword) {
      //  this.id = id;
        Enword = enword;
        Faword = faword;
    }

    public String getEnword() {
        return Enword;
    }

    public void setEnword(String enword) {
        Enword = enword;
    }

    public String getFaword() {
        return Faword;
    }

    public void setFaword(String faword) {
        Faword = faword;
    }
}
