package com.mahyco.cmr_app.utils.searchspinner;


public class GeneralMaster {
    private final String Desc;
    private final String code;

    public GeneralMaster(String code, String Desc){
        this.code = code;
        this.Desc = Desc;
    }
    public String Desc() {
        return Desc;
    }

    public String Code() {
        return code;
    }
    @Override
    public String toString() {
        return Desc;
    }
}
