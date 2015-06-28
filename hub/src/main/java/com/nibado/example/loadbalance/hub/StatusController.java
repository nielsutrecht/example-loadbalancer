package com.nibado.example.loadbalance.hub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nibado.example.loadbalance.hub.NodeList.Node;

@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    private NodeList nodeList;

    @RequestMapping(value = "nodes", method = RequestMethod.GET)
    public List<Node> nodes() {
        return nodeList.getList();
    }
}
