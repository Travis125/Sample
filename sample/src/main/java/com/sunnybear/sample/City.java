package com.sunnybear.sample;

import java.io.Serializable;

/**
 * Created by sunnybear on 16/1/26.
 */
public class City implements Serializable {
    /**
     * lon : 121.4737
     * level : 1
     * address :
     * cityName :
     * alevel : 4
     * lat : 31.23041
     */

    private double lon;
    private int level;
    private String address;
    private String cityName;
    private int alevel;
    private double lat;

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setAlevel(int alevel) {
        this.alevel = alevel;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public int getLevel() {
        return level;
    }

    public String getAddress() {
        return address;
    }

    public String getCityName() {
        return cityName;
    }

    public int getAlevel() {
        return alevel;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return "City{" +
                "lon=" + lon +
                ", level=" + level +
                ", address='" + address + '\'' +
                ", cityName='" + cityName + '\'' +
                ", alevel=" + alevel +
                ", lat=" + lat +
                '}';
    }
}
