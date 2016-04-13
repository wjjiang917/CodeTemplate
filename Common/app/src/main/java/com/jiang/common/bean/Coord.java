
package com.jiang.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Coord {

    private Integer lon;
    private Integer lat;

    /**
     * @return The lon
     */
    public Integer getLon() {
        return lon;
    }

    /**
     * @param lon The lon
     */
    public void setLon(Integer lon) {
        this.lon = lon;
    }

    /**
     * @return The lat
     */
    public Integer getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(Integer lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
