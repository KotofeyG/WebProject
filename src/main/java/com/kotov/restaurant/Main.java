package com.kotov.restaurant;

public class Main {
    public static void main(String[] args) {
        System.out.println("342658Dg".matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,24}$"));
    }
}
