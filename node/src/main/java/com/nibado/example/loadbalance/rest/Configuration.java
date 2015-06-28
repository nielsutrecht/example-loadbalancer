package com.nibado.example.loadbalance.rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
    private static Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private String hostname;
    private int port;
    private final String zookeeperConnectString = "localhost:2181";

    public Configuration() {
        determineHostname();
        determinePort();
    }

    private void determineHostname() {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (final UnknownHostException e) {
            LOG.warn("Could not determine hostname, using 'localhost'");
            hostname = "localhost";
        }
    }

    private void determinePort() {
        String port = System.getProperty("server.port");
        if (port == null) {
            LOG.warn("server.port not set, assuming 8080");
            port = "8080";
        }

        this.port = Integer.parseInt(port);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getZookeeperConnectString() {
        return zookeeperConnectString;
    }

}
