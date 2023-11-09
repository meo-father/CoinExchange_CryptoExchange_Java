package com.bizzan.bitrade.util;

import org.apache.commons.lang.StringUtils;

public class SqlInfusion {
  public static String FilterSqlInfusion(String input) {
    if (input == null || input.trim() == "")
      return ""; 
    if (!StringUtils.isNumeric(input))
      return input.replaceAll("\\b(drop|exec|execute|create|truncate|delete|insert|update)\\b", "`$1`"); 
    return input;
  }
}
