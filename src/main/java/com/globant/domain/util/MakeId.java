package com.globant.domain.util;

public class MakeId {
    public static String makeIdNumber(int numberId) {
        String id;
        if (numberId < 10) {
            id = "000" + numberId;
        } else if (numberId < 100) {
            id = "00" + numberId;
        } else if (numberId < 1000) {
            id = "0" + numberId;
        } else {
            id = "" + numberId;
        }
        return id;
    }
}
