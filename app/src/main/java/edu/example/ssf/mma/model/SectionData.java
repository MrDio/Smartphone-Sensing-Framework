package edu.example.ssf.mma.model;

public class SectionData {
    private double sectionTime;
    private Double nextSectionTime;
    private float sectionForces;
    private Float nextSectionForces;

    public SectionData(double sectionTime, Double nextSectionTime, float sectionForces, Float nextSectionForces){
        this.sectionForces = sectionForces;
        this.nextSectionForces = nextSectionForces;
        this.sectionTime = sectionTime;
        this.nextSectionTime = nextSectionTime;
    }

    public double getSectionTime() {
        return sectionTime;
    }

    public Double getNextSectionTime() {
        return nextSectionTime;
    }

    public float getSectionForces() {
        return sectionForces;
    }

    public Float getNextSectionForces() {
        return nextSectionForces;
    }
}
