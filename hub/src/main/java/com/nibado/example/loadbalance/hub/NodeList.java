package com.nibado.example.loadbalance.hub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class NodeList {
    private final List<Node> nodes;

    public NodeList() {
        final Random random = new Random();
        nodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Node n = new Node();
            n.port = 8090 + i;
            n.host = "192.168.1." + (1 + i);
            n.queue = random.nextInt(20);
            nodes.add(n);
        }
    }

    public List<Node> getList() {
        return Collections.unmodifiableList(nodes);
    }

    public Node select() {
        if (nodes.size() == 0) {
            return null;
        }
        return nodes.get(0);
    }

    public static class Node {
        public String host;
        public int port;
        public int queue;

        public String getUrl() {
            return String.format(Locale.ROOT, "http://%s:%s/", host, port);
        }
    }
}
