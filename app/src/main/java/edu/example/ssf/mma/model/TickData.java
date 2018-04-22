package edu.example.ssf.mma.model;

public class TickData {
    private Float accX;
    private Float accY;
    private long timeStamp;

    public Float getAccX() {
        return accX;
    }

    public void setAccX(Float accX) {
        this.accX = accX;
    }

    public Float getAccY() {
        return accY;
    }

    public void setAccY(Float accY) {
        this.accY = accY;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TickData(){
        accX = 0f;
        accY = 0f;
        timeStamp = 0L;
    }
}
