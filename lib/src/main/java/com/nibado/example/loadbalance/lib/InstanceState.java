package com.nibado.example.loadbalance.lib;

import sun.security.jca.GetInstance;

import java.util.concurrent.atomic.AtomicInteger;

public class InstanceState {
    private int port;
    private String hostname;
    private long count;
    private int minDuration;
    private int maxDuration;
    private int queueSize;

    public InstanceState() {
        minDuration = Integer.MAX_VALUE;
    }

    public InstanceState(String hostname, int port) {
        this();
        this.hostname = hostname;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public String toString() {
        return "InstanceState{" +
                "port=" + port +
                ", hostname='" + hostname + '\'' +
                ", count=" + count +
                ", minDuration=" + minDuration +
                ", maxDuration=" + maxDuration +
                ", queueSize=" + queueSize +
                '}';
    }
}
