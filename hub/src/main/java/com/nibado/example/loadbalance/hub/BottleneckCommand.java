package com.nibado.example.loadbalance.hub;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.nibado.example.loadbalance.lib.RandomImage;
import com.nibado.example.loadbalance.lib.RandomImage.Type;

public class BottleneckCommand extends HystrixCommand<String> {
    private static final Logger LOG = LoggerFactory.getLogger(BottleneckCommand.class);
    private final String node;
    private final RandomImage randomImage = new RandomImage(400, 400, 4);

    public BottleneckCommand(final String node) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BottleneckGroup")).andCommandKey(HystrixCommandKey.Factory.asKey("BottleneckGet"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("BottleneckPool-" + node)).andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(10000).withCircuitBreakerEnabled(false).withFallbackEnabled(true)));

        this.node = node;
    }

    @Override
    protected String run() {
        /*
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
         */
        if (Math.random() < 0.1) {
            throw new RuntimeException();
        }

        try {
            return randomImage.generateDataUrl(Type.PNG);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getFallback() {
        return "img/fail.png";
    }
}
