package com.nibado.example.loadbalance.lib;

import java.util.Random;

public class Test {
    public static void main(String[] argv) throws Exception {
        Random random = new Random();
         ZkInstance instance = new ZkInstance("localhost", 8080);
        instance.connect("localhost:2181");
        for(int i = 0;i < 20;i++) {
            int time = random.nextInt(400) + 100;
            instance.builder().increment().duration(time).update();
            Thread.sleep(time);
        }
    }
}
