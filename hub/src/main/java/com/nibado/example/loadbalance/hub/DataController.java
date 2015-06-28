package com.nibado.example.loadbalance.hub;

import java.io.IOException;
import java.util.Random;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nibado.example.loadbalance.lib.RandomImage;
import com.nibado.example.loadbalance.lib.RandomImage.Type;

@RestController
@RequestMapping("/d")
public class DataController {
    private final Random random = new Random();
    private final RandomImage randomImage = new RandomImage(400, 400, 4);

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/plain")
    public String get() {
        final StringBuilder builder = new StringBuilder(110);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 40; x++) {
                builder.append(random.nextFloat() < 0.5 ? '0' : '1');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @RequestMapping(value = "image", method = RequestMethod.GET, produces = "text/plain")
    public String getImage() throws IOException {
        return randomImage.generateDataUrl(Type.PNG);
    }
}
