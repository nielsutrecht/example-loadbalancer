package com.nibado.example.loadbalance.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bottleneck {
    private static final Bottleneck current;
    private final Status status;

    private final Random random;
    private final Map<String, Object> monitors;

    private Bottleneck() {
        random = new Random();
        monitors = new HashMap<>();
        status = new Status();
        status.minWait = 500;
        status.maxWait = 500;
        status.exceptionChance = 0.0;
    }

    public void access(final String monitor) {
        if (random.nextDouble() < status.exceptionChance) {
            throw new RuntimeException("Something went horrible wrong");
        }

        if (status.maxWait - status.minWait <= 0) {
            return;
        }
        final int wait = random.nextInt(status.maxWait - status.minWait) + status.minWait;

        synchronized (getMonitor(monitor)) {
            try {
                Thread.sleep(wait);
            }
            catch (final InterruptedException e) {

            }
        }
    }

    private Object getMonitor(final String monitor) {
        final Object obj;
        synchronized (monitors) {
            if (!monitors.containsKey(monitor)) {
                monitors.put(monitor, new Object());
            }

            obj = monitors.get(monitor);
        }

        return obj;
    }

    public Status getStatus() {
        return status;
    }

    public static Bottleneck get() {
        return current;
    }

    static {
        current = new Bottleneck();
    }

    public static class Status {
        private int minWait;
        private int maxWait;
        private double exceptionChance;

        public int getMinWait() {
            return minWait;
        }

        public void setMinWait(final int minWait) {
            if (minWait < 0) {
                this.minWait = 0;
            }
            else if (minWait > maxWait) {
                this.minWait = maxWait;
            }
            else {
                this.minWait = minWait;
            }
        }

        public int getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(final int maxWait) {
            if (maxWait < 0) {
                this.maxWait = 0;
            }
            else if (maxWait < minWait) {
                this.maxWait = minWait;
            }
            else {
                this.maxWait = maxWait;
            }
        }

        public double getExceptionChance() {
            return exceptionChance;
        }

        public void setExceptionChance(final double exceptionChance) {
            if (exceptionChance < 0.0) {
                this.exceptionChance = 0.0;
            }
            else if (exceptionChance > 1.0) {
                this.exceptionChance = 1.0;
            }
            else {
                this.exceptionChance = exceptionChance;
            }
        }
    }
}
