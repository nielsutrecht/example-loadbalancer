package com.nibado.example.loadbalance.lib;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class ZkHub implements Watcher {
    private static final String ROOT = "/nodes";
    private static final Logger LOG = LoggerFactory.getLogger(ZkHub.class);
    private ZooKeeper zk;
    private Kryo kryo;
    private NodeListener listener = System.out::println;

    public void connect(final String keeperConnectString) throws IOException {
        zk = new ZooKeeper(keeperConnectString, 2000, this);
        kryo = new Kryo();
        updateChildren();
        subscribe();
    }

    private void subscribe() {
        try {
            zk.exists(ROOT, this);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(final WatchedEvent evt) {
        LOG.info("ZooKeeper event: {}", evt);
        if (evt.getType() == Event.EventType.NodeDataChanged) {
            try {
                updateChildren();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            subscribe();

        }
    }

    public void setNodeListener(final NodeListener listener) {
        this.listener = listener;
    }

    private void updateChildren() throws IOException {
        List<String> children = null;
        try {
            children = zk.getChildren(ROOT, false);
        }
        catch (KeeperException | InterruptedException e) {
            throw new IOException(e);
        }
        final List<NodeState> nodes = children.stream().map((c) -> {
            byte[] data;
            try {
                data = zk.getData(ROOT + "/" + c, false, new Stat());
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
            return kryo.readObject(new Input(new ByteArrayInputStream(data)), NodeState.class);
        }).collect(Collectors.toList());

        listener.listUpdated(nodes);
    }
}
