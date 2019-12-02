package com.verzqli.stringartdrawing;

public class NailPoint {
    public int pointX;
    public int pointY;
    public int position;

    public int cacluteSlope(NailPoint nailPoint) {
        int slope = (this.pointY - nailPoint.pointY) / (this.pointX - nailPoint.pointX);
        return slope;
    }

}
