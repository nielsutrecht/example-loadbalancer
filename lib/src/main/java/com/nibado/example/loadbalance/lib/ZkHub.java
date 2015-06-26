package com.nibado.example.loadbalance.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class ZkHub implements Watcher {
    private static final String ROOT = "/nodes";
    private static final Logger LOG = LoggerFactory.getLogger(ZkHub.class);
    private ZooKeeper zk;
    private Kryo kryo;

    public void connect(String keeperConnectString) throws IOException {
        zk = new ZooKeeper(keeperConnectString, 2000, this);
        kryo = new Kryo();

        subscribe();
    }

    private void subscribe() {
        try {
            zk.exists(ROOT, this);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(WatchedEvent evt) {
        LOG.info("ZooKeeper event: {}", evt);
        if(evt.getType() == Event.EventType.NodeDataChanged) {
            try {
                printChildren();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            subscribe();

        }
    }

    private void printChildren() throws Exception{
         List<String> children = zk.getChildren(ROOT, false);
        for(String c : children) {
            byte[] data = zk.getData(ROOT + "/" + c, false, new Stat());
            InstanceState state = kryo.readObject(new Input(new ByteArrayInputStream(data)), InstanceState.class);
            LOG.info("Child {}, state {}", c, state);
        }
    }
}
