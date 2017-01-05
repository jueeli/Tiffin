package com.svmindlogic.tiffin.tiffinmap.model;

/**
 * Created by akash on 20/12/15.
 */
public class M_MapAddress {


    private String name;
    private String business_name;
    private String food_type;
    private String kitchen_type;
    private String delivery_option;
    private String delivery_radius;
    private String full_tiffin_prize;
    private  String half_tiffin_prize;
    private String opens_at;
    private  String closed_at;
    private  String address;
    private String sunday_close;
    private String mobile;
    private  String created_date;
    private  String published;
    private  String latitude;
    private String longitude;
    private String distance;

    private String oneTime;
    private String twoTime;

    public M_MapAddress(String name, String business_name, String food_type, String kitchen_type, String delivery_option, String delivery_radius, String full_tiffin_prize, String half_tiffin_prize, String opens_at, String closed_at, String address, String sunday_close, String mobile, String created_date, String published,
                        String latitude, String longitude, String distance,String oneTime,String twoTime) {
        this.name = name;
        this.business_name = business_name;
        this.food_type = food_type;
        this.kitchen_type = kitchen_type;
        this.delivery_option = delivery_option;
        this.delivery_radius = delivery_radius;
        this.full_tiffin_prize = full_tiffin_prize;
        this.half_tiffin_prize = half_tiffin_prize;
        this.opens_at = opens_at;
        this.closed_at = closed_at;
        this.address = address;
        this.sunday_close = sunday_close;
        this.mobile = mobile;
        this.created_date = created_date;
        this.published = published;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.oneTime = oneTime;
        this.twoTime = twoTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getKitchen_type() {
        return kitchen_type;
    }

    public void setKitchen_type(String kitchen_type) {
        this.kitchen_type = kitchen_type;
    }

    public String getDelivery_option() {
        return delivery_option;
    }

    public void setDelivery_option(String delivery_option) {
        this.delivery_option = delivery_option;
    }

    public String getDelivery_radius() {
        return delivery_radius;
    }

    public void setDelivery_radius(String delivery_radius) {
        this.delivery_radius = delivery_radius;
    }

    public String getFull_tiffin_prize() {
        return full_tiffin_prize;
    }

    public void setFull_tiffin_prize(String full_tiffin_prize) {
        this.full_tiffin_prize = full_tiffin_prize;
    }

    public String getHalf_tiffin_prize() {
        return half_tiffin_prize;
    }

    public void setHalf_tiffin_prize(String half_tiffin_prize) {
        this.half_tiffin_prize = half_tiffin_prize;
    }

    public String getOpens_at() {
        return opens_at;
    }

    public void setOpens_at(String opens_at) {
        this.opens_at = opens_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSunday_close() {
        return sunday_close;
    }

    public void setSunday_close(String sunday_close) {
        this.sunday_close = sunday_close;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOneTime() {
        return oneTime;
    }

    public void setOneTime(String oneTime) {
        this.oneTime = oneTime;
    }

    public String getTwoTime() {
        return twoTime;
    }

    public void setTwoTime(String twoTime) {
        this.twoTime = twoTime;
    }
}
