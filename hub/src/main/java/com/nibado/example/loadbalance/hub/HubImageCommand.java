package com.nibado.example.loadbalance.hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

public class HubImageCommand extends HystrixCommand<String> {
    private static final Logger LOG = LoggerFactory.getLogger(NodeImageCommand.class);
    private final NodeList list;

    public HubImageCommand(final NodeList list) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Hub")).andCommandKey(HystrixCommandKey.Factory.asKey("HubImage")).andThreadPoolKey(
            HystrixThreadPoolKey.Factory.asKey("HubImage")).andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000).withCircuitBreakerEnabled(false).withFallbackEnabled(true)));

        this.list = list;
    }

    @Override
    protected String run() {
        final String node = list.select().getUrl();
        if (node == null) {
            throw new IllegalStateException("No node available");
        }
        return new NodeImageCommand(node).execute();
    }

    @Override
    protected String getFallback() {
        LOG.warn("Fallback");
        return "img/fail.png";
    }
}
