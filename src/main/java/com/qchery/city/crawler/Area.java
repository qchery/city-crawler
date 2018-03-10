package com.qchery.city.crawler;

import java.io.Serializable;

public class Area implements Serializable {
    private int id;
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

    public Area(int id, String name, ExecutiveLevel executiveLevel) {
        this.id = id;
        this.name = name;
        this.executiveLevel = executiveLevel;
    }

    public Area(int id, String name, String code, ExecutiveLevel executiveLevel) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.executiveLevel = executiveLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ExecutiveLevel getExecutiveLevel() {
        return executiveLevel;
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
