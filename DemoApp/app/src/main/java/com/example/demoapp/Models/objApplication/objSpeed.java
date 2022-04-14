package com.example.demoapp.Models.objApplication;


import java.util.Calendar;
import java.util.Random;

public class objSpeed {
    private int id;
    private double speed;
    private long time;

    public objSpeed(double speed) {
        this.id = new Random().nextInt();
        this.speed = round(speed,2);
        this.time = Calendar.getInstance().getTimeInMillis();
    }

    public objSpeed(int id, double speed, long time) {
        this.id = id;
        this.speed = speed;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = round(speed,2);
    }

    public String getStringSpeed(){
        return this.getSpeed() + " km/hr";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
