package edu.example.ssf.mma.data;

public class Section {
    private final float LONGCURVETHRESHOLD = 2.0f;

    public void calculateCurveType(){
        if(median.getAccX() > 0){
            if(timeTaken > LONGCURVETHRESHOLD)
                setType(SectionType.LONGRIGHTCURVE);
            setType(SectionType.RIGHTCURVE);
        } else{
            if(timeTaken > LONGCURVETHRESHOLD)
                setType(SectionType.LONGLEFTCURVE);
            setType(SectionType.LEFTCURVE);
        }

    }

    public enum SectionType {
        STRAIGHT,
        RIGHTCURVE,
        LEFTCURVE,
        LONGLEFTCURVE,
        LONGRIGHTCURVE,
        UNDEFINED,
        INVALID
    }

    private SectionType type;
    private String optimizationTip;
    private double performanceIndicator;
    private TickData start;
    private TickData end;
    private TickData median;
    private double timeTaken;
    private double forceToVehicle;

    public Section(){
        type = SectionType.UNDEFINED;
    }

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

    public TickData getStart() {
        return start;
    }

    public void setStart(TickData start) {
        this.start = start;
    }

    public TickData getEnd() {
        return end;
    }

    public void setEnd(TickData end) {
        this.end = end;
    }

    public TickData getMedian() {
        return median;
    }

    public void setMedian(TickData median) {
        this.median = median;
    }

    public double getForceToVehicle() {
        return forceToVehicle;
    }

    public void setForceToVehicle(double forceToVehicle) {
        this.forceToVehicle = forceToVehicle;
    }

    public double getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken() {
        this.timeTaken = end.getTimeStamp()-start.getTimeStamp();
    }
}
