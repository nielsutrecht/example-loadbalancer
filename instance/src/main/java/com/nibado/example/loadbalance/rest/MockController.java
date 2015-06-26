package com.nibado.example.loadbalance.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class MockController {
    private static Logger LOG = LoggerFactory.getLogger(MockController.class);

    @RequestMapping(value = "/bottleneck/{monitor}/{id}", method= RequestMethod.GET)
    public MockResponse bottleneck(@PathVariable String monitor, @PathVariable String id) {
        return get(monitor, id);
    }

    @RequestMapping(value = "/bottleneck/{monitor}/{id}/deferred", method = RequestMethod.GET)
    public DeferredResult<MockResponse> bottleneckDeferred(@PathVariable String monitor, @PathVariable String id) {
        final DeferredResult<MockResponse> result = new DeferredResult<>();

        new Thread(() -> {
            result.setResult(get(monitor, id));
        }).start();

        return result;
    }

    private MockResponse get(String monitor, String id) {
        long start = System.currentTimeMillis();
        Bottleneck.get().access(monitor);
        long time = System.currentTimeMillis() - start;
        LOG.info("Bottleneck access on '{}' completed after {} ms", monitor, time);
        return new MockResponse(200, id, System.currentTimeMillis() - start);
    }
}
