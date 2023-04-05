package com.example.testdisasterevent.data;

public class UploadImage {
    private String mimagename;
    private String mimageuri;


    public  UploadImage(){

    }


    public UploadImage(String name, String imageuri) {
        if(name.trim().equals("")){
            name="noName";
        }
        mimagename = name;
        mimageuri = imageuri;
    }

    public String getMimagename() {
        return mimagename;
    }

    public void setMimagename(String mimagename) {
        this.mimagename = mimagename;
    }

    public String getMimageuri() {
        return mimageuri;
    }

    public void setMimageuri(String mimageuri) {
        this.mimageuri = mimageuri;
    }
}
