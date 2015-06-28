package com.nibado.example.loadbalance.hub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.nibado.example.loadbalance.lib.NodeListener;
import com.nibado.example.loadbalance.lib.NodeState;

@Component
public class NodeList implements NodeListener {
    private List<NodeState> nodes;
    private final Random random = new Random();

    public NodeList() {

        nodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final NodeState n = new NodeState("192.168.1." + (1 + i), 9000 + i);
            n.setQueueSize(random.nextInt(20));

            nodes.add(n);
        }
    }

    public List<NodeState> getList() {
        return Collections.unmodifiableList(nodes);
    }

    public NodeState select() {
        return nodes.get(random.nextInt(nodes.size()));
    }

    @Override
    public void listUpdated(final List<NodeState> nodes) {
        this.nodes = nodes;
    }
}
