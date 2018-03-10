package com.qchery.city.crawler.enums;

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

    public static ExecutiveLevel parse(int level) {
        for (ExecutiveLevel executiveLevel : values()) {
            if (executiveLevel.getLevel() == level) {
                return executiveLevel;
            }
        }
        return null;
    }
}
