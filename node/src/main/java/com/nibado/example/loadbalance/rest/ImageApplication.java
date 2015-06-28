package com.nibado.example.loadbalance.rest;

import java.net.InetAddress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageApplication {
    public static void main(final String[] args) throws Exception {
        System.out.println(InetAddress.getLocalHost().getHostName());
        SpringApplication.run(ImageApplication.class, args);
    }
}
