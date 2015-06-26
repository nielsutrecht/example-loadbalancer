package com.nibado.example.loadbalance.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.zookeeper.*;
import org.apache.zookeeper.server.ByteBufferOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ZkInstance implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(ZkInstance.class);
    private ZooKeeper zk;
    private Kryo kryo;
    private String hostname;
    private int port;
    private InstanceState state;
    private String zNode;

    public ZkInstance(String hostname, int port) throws Exception {
        this.port = port;
        this.hostname = hostname;
        this.state = new InstanceState(hostname, port);

        kryo = new Kryo();

    }

    private void persistState() throws KeeperException, InterruptedException {
        zk.setData(zNode, getStateBytes(), -1);
    }

    private byte[] getStateBytes() {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Output out = new Output(outStream);
        kryo.writeObject(out, state);
        out.flush();
        return outStream.toByteArray();
    }

    private void createRoot() throws KeeperException, InterruptedException {
        try {
            zk.create("/nodes", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        catch(KeeperException e) {
            if(e.code() != KeeperException.Code.NODEEXISTS) {
                throw e;
            }
        }
        zNode = zk.create("/nodes/node", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        LOG.info("Created node with zNode {}", zNode);
    }



    public void connect(String keeperConnectString) throws IOException {
        zk = new ZooKeeper(keeperConnectString, 2000, this);
        try {
            createRoot();
        }
        catch(Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void process(WatchedEvent evt) {
        LOG.info("ZooKeeper event: {}", evt.getState());
    }

    public Builder builder() {
        return new Builder();
    }

    public class Builder {
        public Builder increment() {
            state.setCount(state.getCount() + 1);

            return this;
        }

        public Builder duration(int ms) {
            if(state.getMinDuration() > ms) {
                state.setMinDuration(ms);
            }
            if(state.getMaxDuration() < ms) {
                state.setMaxDuration(ms);
            }

            return this;
        }
        public void update() throws IOException {
            try {
                persistState();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }
}
