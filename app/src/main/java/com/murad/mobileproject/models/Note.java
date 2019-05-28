package com.murad.mobileproject.models;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note implements Serializable {
    private String title ;
    private String content;
    private Date dateTime ;
    private String stringDate;
    private String adress ;
    private String color  = "#FFFFFF";
    private boolean isPrior;
    private Reminder reminder;
    private String photoUri ;
    private String videoUri ;
    private String audioUri ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getStringDate() {

        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPrior() {
        return isPrior;
    }

    public void setPrior(boolean prior) {
        isPrior = prior;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public static List<Note> parse(Context context, JSONArray jsonArray){
        List<Note> noteList = new ArrayList<>();

        if (jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Gson gson1 = new Gson();
                    Note note = gson1.fromJson(String.valueOf(jsonObject), Note.class);
                    noteList.add(note);
                } catch (Exception e){
                    noteList = new ArrayList<>();
                    return noteList;
                }
            }
        }
        return noteList;
    }
}
