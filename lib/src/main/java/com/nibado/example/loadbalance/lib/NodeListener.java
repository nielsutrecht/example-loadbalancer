package com.nibado.example.loadbalance.lib;

import java.util.List;

public interface NodeListener {
    public void listUpdated(List<NodeState> list);
}
