package edu.example.ssf.mma.data;

public class Section {
    public enum SectionType {
        STRAIGHT,
        RIGHTCURVE,
        LEFTCURVE
    }

    private SectionType type;
    private String optimizationTip;
    private double performanceIndicator;

    public Section(SectionType sectionType, String optimizationTip, double performanceIndicator){
        this.performanceIndicator = performanceIndicator;
        this.type = sectionType;
        this.optimizationTip = optimizationTip;
    }
    public SectionType getType() {
        return type;
    }

    public void setType(SectionType type) {
        this.type = type;
    }

    public int getPerformanceIndicator() {
        if(performanceIndicator > 0.75){
            return 2;
        }
        if(performanceIndicator > 0.25){
            return 1;
        }
        else{
            return 0;
        }
    }

    public void setPerformanceIndicator(double performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }


    public String getOptimizationTip() {
        return optimizationTip;
    }

    public void setOptimizationTip(String optimizationTip) {
        this.optimizationTip = optimizationTip;
    }
}
