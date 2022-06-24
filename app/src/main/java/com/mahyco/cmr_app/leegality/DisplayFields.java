package com.mahyco.cmr_app.leegality;

public class DisplayFields {

    public String key;
    public String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DisplayFields{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
