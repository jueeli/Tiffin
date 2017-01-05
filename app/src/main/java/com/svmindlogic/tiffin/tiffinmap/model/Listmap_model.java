package com.svmindlogic.tiffin.tiffinmap.model;

/**
 * Created by pappu on 20/12/15.
 */
public class Listmap_model {
    String owner_name;
    String Business_name;
    String Food_type;
    String Kitchen_type;
    String Delivery_option;
    String Delivery_radius;
    String Full_tiffin_prize;
    String Half_tiffin_prize;
    String Opens_at;
    String Closed_at;

    String Distance;
    String Address;
    String Sunday_close;
    String Mobile;
    private String oneTime;
    private String twoTime;


    public Listmap_model(String owner_name, String Business_name, String Food_type, String Kitchen_type,
                         String Delivery_option, String Delivery_radius, String
                                 Full_tiffin_prize, String Half_tiffin_prize, String Opens_at, String Closed_at, String Distance,
                         String Address, String Sunday_close, String Mobile, String oneTime, String twoTime) {


        this.owner_name = owner_name;
        this.Business_name = Business_name;
        this.Food_type = Food_type;
        this.Kitchen_type = Kitchen_type;
        this.Delivery_option = Delivery_option;
        this.Delivery_radius = Delivery_radius;
        this.Full_tiffin_prize = Full_tiffin_prize;
        this.Half_tiffin_prize = Half_tiffin_prize;
        this.Opens_at = Opens_at;
        this.Closed_at = Closed_at;
        this.Distance = Distance;
        this.Address = Address;
        this.Sunday_close = Sunday_close;
        this.Mobile = Mobile;
        this.oneTime = oneTime;
        this.twoTime = twoTime;
    }

    public String getFood_type() {
        return Food_type;
    }

    public void setFood_type(String food_type) {
        Food_type = food_type;
    }

    public String getKitchen_type() {
        return Kitchen_type;
    }

    public void setKitchen_type(String kitchen_type) {
        Kitchen_type = kitchen_type;
    }

    public String getDelivery_radius() {
        return Delivery_radius;
    }

    public void setDelivery_radius(String delivery_radius) {
        Delivery_radius = delivery_radius;
    }

    public String getDelivery_option() {
        return Delivery_option;
    }

    public void setDelivery_option(String delivery_option) {
        Delivery_option = delivery_option;
    }

    public String getFull_tiffin_prize() {
        return Full_tiffin_prize;
    }

    public void setFull_tiffin_prize(String full_tiffin_prize) {
        Full_tiffin_prize = full_tiffin_prize;
    }

    public String getHalf_tiffin_prize() {
        return Half_tiffin_prize;
    }

    public void setHalf_tiffin_prize(String half_tiffin_prize) {
        Half_tiffin_prize = half_tiffin_prize;
    }

    public String getClosed_at() {
        return Closed_at;
    }

    public void setClosed_at(String closed_at) {
        Closed_at = closed_at;
    }

    public String getOpens_at() {
        return Opens_at;
    }

    public void setOpens_at(String opens_at) {
        Opens_at = opens_at;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSunday_close() {
        return Sunday_close;
    }

    public void setSunday_close(String sunday_close) {
        Sunday_close = sunday_close;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getBusiness_name() {
        return Business_name;

    }

    public void setBusiness_name(String business_name) {
        Business_name = business_name;
    }

    public String getOwner_name() {
        return owner_name;

    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
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
