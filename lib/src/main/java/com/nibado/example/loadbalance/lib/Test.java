package com.nibado.example.loadbalance.lib;

import java.util.Random;

public class Test {
    public static void main(String[] argv) throws Exception {
        Random random = new Random();
        ZkInstance instance = new ZkInstance("localhost", 8080);
        ZkHub hub = new ZkHub();
        new Thread(() -> {
            try {
                hub.connect("localhost:2181");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }).start();

        instance.connect("localhost:2181");
        for(int i = 0;i < 2000;i++) {
            int time = random.nextInt(500) + 500;
            instance.builder().increment().duration(time).queueSize(random.nextInt(20)).update();
            Thread.sleep(time);
        }
    }
}
