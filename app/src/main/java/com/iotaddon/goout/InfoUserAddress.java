package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-08.
 */

public class InfoUserAddress {
    private double x;
    private double y;
    private String name;

    public InfoUserAddress(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
