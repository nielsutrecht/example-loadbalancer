package com.nibado.example.loadbalance.hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/image")
public class ImageController {
    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private NodeList nodeList;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/plain")
    public DeferredResult<String> bottleneck() {
        final DeferredResult<String> result = new DeferredResult<>();

        final String node = nodeList.select().getUrl();

        final BottleneckCommand cmd = new BottleneckCommand(node);
        cmd.observe().subscribe((v) -> {
            result.setResult(v);
        });
        LOG.trace("Request");
        return result;
    }
}
