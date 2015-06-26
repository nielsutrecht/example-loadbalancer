package com.nibado.example.loadbalance.rest;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bottleneck {
    private static final Bottleneck current;
    private static final int MIN_WAIT = 1000;
    private static final int MAX_WAIT = 5000;
    private static final double EXCEPTION_CHANCE = 0.0;

    private Random random;
    private Map<String, Object> monitors;

    private Bottleneck() {
        random = new Random();
        monitors = new HashMap<>();
    }

    public void access(String monitor) {
        if(random.nextDouble() < EXCEPTION_CHANCE) {
            throw new RuntimeException("Something went horrible wrong");
        }
        int wait = random.nextInt(MAX_WAIT - MIN_WAIT) + MIN_WAIT;

        synchronized (getMonitor(monitor)) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {

            }
        }
    }

    private Object getMonitor(String monitor) {
        final Object obj;
        synchronized (monitors) {
            if(!monitors.containsKey(monitor)) {
                monitors.put(monitor, new Object());
            }

            obj = monitors.get(monitor);
        }

        return obj;
    }

    public static Bottleneck get() {
        return current;
    }

    static {
        current = new Bottleneck();
    }
}
