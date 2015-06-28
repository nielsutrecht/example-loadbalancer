package com.nibado.example.loadbalance.lib;

import java.util.Locale;

public class NodeState {
    private int port;
    private String hostname;
    private long count;
    private int minDuration;
    private int maxDuration;
    private int queueSize;

    public NodeState() {
        minDuration = Integer.MAX_VALUE;
    }

    public NodeState(final String hostname, final int port) {
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

    public void setCount(final long count) {
        this.count = count;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(final int minDuration) {
        this.minDuration = minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(final int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(final int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public String toString() {
        return "NodeState{" + "port=" + port + ", hostname='" + hostname + '\'' + ", count=" + count + ", minDuration=" + minDuration + ", maxDuration="
            + maxDuration + ", queueSize=" + queueSize + '}';
    }

    public String getUrl() {
        return String.format(Locale.ROOT, "http://%s:%s", hostname, port);
    }
}
