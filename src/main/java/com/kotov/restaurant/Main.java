package com.kotov.restaurant;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    List<String> list = Collections.synchronizedList(new LinkedList<>());
    System.out.println(list.getClass().getSimpleName());
    new ArrayList<>();

    }
}