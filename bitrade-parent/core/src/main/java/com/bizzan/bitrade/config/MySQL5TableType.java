package com.bizzan.bitrade.config;

import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.stereotype.Component;

@Component
public class MySQL5TableType extends MySQL5Dialect {

    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}