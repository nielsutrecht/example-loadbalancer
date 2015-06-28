package com.nibado.example.loadbalance.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class ZkNode implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(ZkNode.class);
    private ZooKeeper zk;
    private final Kryo kryo;
    private final NodeState state;
    private String zNode;

    public ZkNode(final String hostname, final int port) throws Exception {
        this.state = new NodeState(hostname, port);

        kryo = new Kryo();
    }

    private void persistState() throws KeeperException, InterruptedException {
        zk.setData(zNode, getStateBytes(), -1);
        zk.setData("/nodes", new byte[0], -1);
        LOG.debug("Updated node {}", zNode);
    }

    private byte[] getStateBytes() {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final Output out = new Output(outStream);
        kryo.writeObject(out, state);
        out.flush();
        return outStream.toByteArray();
    }

    private void createRoot() throws KeeperException, InterruptedException {
        try {
            zk.create("/nodes", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        catch (final KeeperException e) {
            if (e.code() != KeeperException.Code.NODEEXISTS) {
                throw e;
            }
        }
        zNode = zk.create("/nodes/node", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        LOG.info("Created node with zNode {}", zNode);
    }

    public void connect(final String keeperConnectString) throws IOException {
        LOG.info("Connecting to zookepers on {}", keeperConnectString);
        zk = new ZooKeeper(keeperConnectString, 2000, this);
        try {
            createRoot();
        }
        catch (final Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void process(final WatchedEvent evt) {
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

        public Builder duration(final int ms) {
            if (state.getMinDuration() > ms) {
                state.setMinDuration(ms);
            }
            if (state.getMaxDuration() < ms) {
                state.setMaxDuration(ms);
            }

            return this;
        }

        public Builder queueSize(final int size) {
            state.setQueueSize(size);

            return this;
        }

        public void update() throws IOException {
            try {
                persistState();
            }
            catch (final Exception e) {
                throw new IOException(e);
            }
        }
    }
}
