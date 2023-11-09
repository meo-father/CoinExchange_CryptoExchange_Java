package com.bizzan.bitrade.sql;

public class Parameter {
    public int type;
    public ParameterDirection direction;
    public Object value;

    public Parameter(int type, ParameterDirection direction, Object value) {
        this.type = type;
        this.direction = direction;
        this.value = value;
    }
}