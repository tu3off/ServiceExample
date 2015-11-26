package com.tu3off.serviceexample.restore;

public class RestoreManager {

    private String value;

    public synchronized void setValue(String value) {
        this.value = value;
    }

    public synchronized String getValue() {
        return value;
    }
}
