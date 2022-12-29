
package com.Project.veganandglutenfree;

public class Buiss {
    public String name,address,type,image;
    public Integer id;
    public Double lat,lon;


    public Buiss() {

    }

    public Buiss(String name,String address,String type,Double lat,Double lon,Integer id) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.image = image;
        this.lat = lat;
        this.lon = lon;
        this.id = id;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Double getLat() { return lat;}

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}