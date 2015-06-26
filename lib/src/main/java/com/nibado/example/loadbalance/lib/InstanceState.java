package com.nibado.example.loadbalance.lib;

import sun.security.jca.GetInstance;

public class InstanceState {
    private int port;
    private String hostname;
    private long count;
    private int minDuration;
    private int maxDuration;

    public InstanceState(String hostname, int port) {
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
}
