package com.qchery.city.crawler;

public class Area {
    /**
     * 名称
     */
    private String name;
    /**
     * 行政代码
     */
    private String code;
    /**
     * 行政级别
     */
    private ExecutiveLevel executiveLevel;

    private Area parentArea;

    public Area(String name, ExecutiveLevel executiveLevel) {
        this.name = name;
        this.executiveLevel = executiveLevel;
    }

    public Area(String name, String code, ExecutiveLevel executiveLevel) {
        this.name = name;
        this.code = code;
        this.executiveLevel = executiveLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ExecutiveLevel getExecutiveLevel() {
        return executiveLevel;
    }

    public void setExecutiveLevel(ExecutiveLevel executiveLevel) {
        this.executiveLevel = executiveLevel;
    }

    public Area getParentArea() {
        return parentArea;
    }

    public void setParentArea(Area parentArea) {
        this.parentArea = parentArea;
    }

    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", executiveLevel=" + executiveLevel +
                ", parentArea=" + parentArea +
                '}';
    }
}
