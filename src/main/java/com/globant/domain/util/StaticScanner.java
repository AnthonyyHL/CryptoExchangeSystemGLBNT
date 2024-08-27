package com.globant.domain.util;

import java.util.Scanner;

public class StaticScanner {
    private static Scanner instance;
    private StaticScanner() {
    }
    public static Scanner getInstance() {
        if (instance == null)
            instance = new Scanner(System.in);
        return instance;
    }
}
