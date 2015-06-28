package com.nibado.example.loadbalance.rest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nibado.example.loadbalance.lib.RandomImage;
import com.nibado.example.loadbalance.lib.RandomImage.Type;
import com.nibado.example.loadbalance.lib.ZkNode;
import com.nibado.example.loadbalance.rest.Bottleneck.Status;

@RestController
public class ImageController {
    private static Logger LOG = LoggerFactory.getLogger(ImageController.class);
    private static AtomicInteger queueSize = new AtomicInteger(0);
    private final RandomImage randomImage = new RandomImage(400, 400, 4);

    @Autowired
    private Configuration config;
    private ZkNode node;

    @PostConstruct
    public void init() {
        try {
            node = new ZkNode(config.getHostname(), config.getPort());
            node.connect(config.getZookeeperConnectString());
            node.builder().update();
        }
        catch (final Exception e) {
            LOG.error("Error connecting to zookeeper", e);
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = "text/plain")
    public String image() {
        queueSize.incrementAndGet();
        int duration = -1;
        try {
            final long start = System.currentTimeMillis();
            Bottleneck.get().access("image");
            final String result = randomImage.generateDataUrl(Type.PNG);
            duration = (int) (System.currentTimeMillis() - start);
            return result;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            updateStats(duration, queueSize.decrementAndGet());
        }
    }

    private void updateStats(final int duration, final int queue) {
        try {
            node.builder().increment().duration(duration).queueSize(queue).update();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/configure/min/{min}", method = RequestMethod.GET)
    public Status configureMinWait(@PathVariable final int min) {
        Bottleneck.get().getStatus().setMinWait(min);
        return Bottleneck.get().getStatus();
    }

    @RequestMapping(value = "/configure/max/{max}", method = RequestMethod.GET)
    public Status configureMaxWait(@PathVariable final int max) {
        Bottleneck.get().getStatus().setMaxWait(max);
        return Bottleneck.get().getStatus();
    }

    @RequestMapping(value = "/configure/exception/{chance}", method = RequestMethod.GET)
    public Status configureException(@PathVariable final int chance) {
        Bottleneck.get().getStatus().setExceptionChance(chance / 100.0);
        return Bottleneck.get().getStatus();
    }

}
