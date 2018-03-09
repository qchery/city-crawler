package com.qchery.city.crawler;

public enum ExecutiveLevel {

    /**
     * 省
     */
    PROVINCE(1),
    /**
     * 市
     */
    CITY(2),
    /**
     * 区
     */
    COUNTY(3);

    private int level;

    ExecutiveLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
