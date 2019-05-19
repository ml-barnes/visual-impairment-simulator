package com.op.barnm7.vis;

public class ImpairmentData {

    private String[] imageNames;
    private String impairmentName;
    private String backgroundImageName;

    public ImpairmentData(String[] imageNames, String impairmentName, String backgroundImageName) {
        this.imageNames = imageNames;
        this.impairmentName = impairmentName;
        this.backgroundImageName = backgroundImageName;
    }

    public String[] getImageNames() {
        return imageNames;
    }

    public void setImageNames(String[] imageNames) {
        this.imageNames = imageNames;
    }

    public String getImpairmentName() {
        return impairmentName;
    }

    public void setImpairmentName(String impairmentName) {
        this.impairmentName = impairmentName;
    }

    public String getBackgroundImageName() {
        return backgroundImageName;
    }

    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }
}
