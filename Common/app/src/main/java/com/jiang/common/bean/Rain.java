package com.jiang.common.bean;


import org.apache.commons.lang3.builder.ToStringBuilder;

public class Rain {

    private Integer _3h;

    /**
     * @return The _3h
     */
    public Integer get3h() {
        return _3h;
    }

    /**
     * @param _3h The 3h
     */
    public void set3h(Integer _3h) {
        this._3h = _3h;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
