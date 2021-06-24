package com.huawei.shopmate.model;

public class SiteData {

    String name;
    String address;
    String phone;
    Double distance;
    Double rating;

    public SiteData(String name, String address, String phone, Double distance, Double rating) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.distance = distance;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
