package edu.example.ssf.mma.model;

public class Section {


    private SectionType type;
    private SectionPerformance sectionPerformance;
    private SectionSpeed sectionSpeed;
    private CurveGrade curveGrade;
    private String optimizationTip;
    private double performanceIndicator;
    private TickData start;
    private TickData end;
    private TickData median;
    private double timeTaken;
    private double forceToVehicle;

    public Section() {
        type = SectionType.UNDEFINED;
        curveGrade = CurveGrade.NOTAVAILABLE;
        sectionSpeed = SectionSpeed.NOTAVAILABLE;
    }

    public Section(SectionType sectionType, String optimizationTip, double performanceIndicator) {
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
        if (performanceIndicator > 0.75) {
            return 2;
        }
        if (performanceIndicator > 0.25) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setPerformanceIndicator(double performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }


    public String getOptimizationTip() {
        String tip;
        boolean isTip = true;
        switch (type) {
            case LEFTCURVE:
            case RIGHTCURVE:
                tip = "turn";
                break;
            case STRAIGHT:
                tip = "straight";
                break;
            default:
                isTip = false;
                tip = "No tip available.";
        }

        if (isTip)
            switch (sectionSpeed) {
                case TOOFAST:
                    if (tip.equals("turn")) {
                        tip = "Make the turn a little slower to accelerate on the straight.";
                    } else {
                        tip = "Slow down on the straight to improve cornering.";
                    }
                    break;
                case FAST:
                    tip = "This was impressive!";
                    break;
                case GOOD:
                    tip = "Try to be a little faster on the " + tip + ".";
                    break;
                case SLOW:
                    tip = "Put the pedal to the metal!";
                    break;
                default:
                    tip = "No tip available.";
            }

        return tip;
    }

//    public void setOptimizationTip(String optimizationTip) {
//        this.optimizationTip = optimizationTip;
//    }

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
        this.timeTaken = end.getTimeStamp() - start.getTimeStamp();
    }

    public SectionPerformance getSectionPerformance() {
        return sectionPerformance;
    }

    public void setSectionPerformance(SectionPerformance sectionPerformance) {
        this.sectionPerformance = sectionPerformance;
    }

    public SectionSpeed getSectionSpeed() {
        return sectionSpeed;
    }

    public void setSectionSpeed(SectionSpeed sectionSpeed) {
        this.sectionSpeed = sectionSpeed;
    }

    public CurveGrade getCurveGrade() {
        return curveGrade;
    }

    public void setCurveGrade(CurveGrade curveGrade) {
        this.curveGrade = curveGrade;
    }
}
