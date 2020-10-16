package com.smartbizz.newUI.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Templatemsg {
    @SerializedName("category")
    private String category;

    @SerializedName("sms")
    private String sms;

    @Expose
    @SerializedName("icon")
    private String icon;

    @SerializedName("id")
    private String id;


    public Templatemsg(JSONObject jsonObject) {
        if (jsonObject != null) {
            category = jsonObject.optString("category");
            sms = jsonObject.optString("sms");
            icon = jsonObject.optString("icon");
            id = jsonObject.optString("id");
        }
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
