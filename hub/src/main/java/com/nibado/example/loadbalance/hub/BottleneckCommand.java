package com.nibado.example.loadbalance.hub;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

public class BottleneckCommand extends HystrixCommand<String> {
    private static final Logger LOG = LoggerFactory.getLogger(BottleneckCommand.class);
    private final String node;

    public BottleneckCommand(final String node) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BottleneckGroup")).andCommandKey(HystrixCommandKey.Factory.asKey("Image"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("BottleneckPool-" + node)).andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000).withCircuitBreakerEnabled(false).withFallbackEnabled(true)));

        this.node = node;
    }

    @Override
    protected String run() {

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet request = new HttpGet(node + "d/image");

        try {
            final HttpResponse response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        }
        catch (final Exception e) {
            LOG.error("Error in GET request", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getFallback() {
        return "img/fail.png";
    }
}
