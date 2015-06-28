package com.nibado.example.loadbalance.hub;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @RequestMapping(value = "nodes", method = RequestMethod.GET)
    public List<Node> nodes() {
        final List<Node> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final Node n = new Node();
            n.port = 8090 + i;
            n.host = "192.168.1." + (1 + i);

            list.add(n);
        }

        return list;
    }

    public static class Node {
        public String host;
        public int port;
    }

}
