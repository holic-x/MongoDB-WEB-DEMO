package com.eb.framework.enums;

public enum MongoConst {
    GT("$gt"),
    LT("$lt"),
    GTE("$gte"),
    LTE("$lte"),
    AND("and"),
    OR("or"),
    NOT("not");
    private String compareIdentify;

    MongoConst(String compareIdentify) {
        this.compareIdentify = compareIdentify;
    }
    public String getCompareIdentify() {
        return compareIdentify;
    }
}