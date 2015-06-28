package com.nibado.example.loadbalance.hub;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.HystrixThreadPoolMetrics;
import com.nibado.example.loadbalance.lib.NodeState;
import com.nibado.example.loadbalance.lib.ZkHub;

@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    private NodeList nodeList;

    private ZkHub hub;

    @PostConstruct
    public void init() throws IOException {
        hub = new ZkHub();
        hub.setNodeListener(nodeList);
        hub.connect("localhost:2181");
    }

    @RequestMapping(value = "nodes", method = RequestMethod.GET)
    public List<NodeState> nodes() {
        return nodeList.getList();
    }

    @RequestMapping(value = "/commands", method = RequestMethod.GET)
    public List<HystrixCommandStats> commandStats() {
        return HystrixCommandMetrics.getInstances().stream().map((m) -> {
            final HystrixCommandStats s = new HystrixCommandStats();
            s.currentConcurrentExecutionCount = m.getCurrentConcurrentExecutionCount();
            s.commandGroup = m.getCommandGroup().name();
            s.commandKey = m.getCommandKey().name();
            s.executionTimeMean = m.getExecutionTimeMean();
            s.errorCount = m.getHealthCounts().getErrorCount();
            s.totalCount = m.getHealthCounts().getTotalRequests();
            s.errorPercentage = m.getHealthCounts().getErrorPercentage();
            s.totalTimeMean = m.getTotalTimeMean();

            return s;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/threads", method = RequestMethod.GET)
    public List<HystrixThreadStats> threadStats() {
        return HystrixThreadPoolMetrics.getInstances().stream().map((m) -> {
            final HystrixThreadStats s = new HystrixThreadStats();

            s.poolName = m.getThreadPoolKey().name();
            s.cumulativeExecuted = m.getCumulativeCountThreadsExecuted();
            s.currentActiveCount = m.getCurrentActiveCount().intValue();
            s.currentCompletedCount = m.getCurrentCompletedTaskCount().intValue();
            s.currentCorePoolSize = m.getCurrentCorePoolSize().intValue();
            s.currentLargestPoolSize = m.getCurrentLargestPoolSize().intValue();
            s.currentMaxPoolSize = m.getCurrentMaximumPoolSize().intValue();
            s.currentPoolSize = m.getCurrentPoolSize().intValue();
            s.currentQueueSize = m.getCurrentQueueSize().intValue();
            s.currentTaskCount = m.getCurrentTaskCount().intValue();

            return s;
        }).collect(Collectors.toList());
    }

    public static class HystrixThreadStats {
        private String poolName;
        private long cumulativeExecuted;
        private int currentActiveCount;
        private int currentCompletedCount;
        private int currentCorePoolSize;
        private int currentLargestPoolSize;
        private int currentMaxPoolSize;
        private int currentPoolSize;
        private int currentQueueSize;
        private int currentTaskCount;

        public String getPoolName() {
            return poolName;
        }

        public long getCumulativeExecuted() {
            return cumulativeExecuted;
        }

        public int getCurrentActiveCount() {
            return currentActiveCount;
        }

        public int getCurrentCompletedCount() {
            return currentCompletedCount;
        }

        public int getCurrentCorePoolSize() {
            return currentCorePoolSize;
        }

        public int getCurrentLargestPoolSize() {
            return currentLargestPoolSize;
        }

        public int getCurrentMaxPoolSize() {
            return currentMaxPoolSize;
        }

        public int getCurrentPoolSize() {
            return currentPoolSize;
        }

        public int getCurrentQueueSize() {
            return currentQueueSize;
        }

        public int getCurrentTaskCount() {
            return currentTaskCount;
        }
    }

    public static class HystrixCommandStats {
        private String commandGroup;
        private String commandKey;
        private int currentConcurrentExecutionCount;
        private long errorCount;
        private long totalCount;
        private int errorPercentage;
        private int executionTimeMean;
        private int totalTimeMean;

        public HystrixCommandStats() {

        }

        public int getCurrentConcurrentExecutionCount() {
            return currentConcurrentExecutionCount;
        }

        public String getCommandGroup() {
            return commandGroup;
        }

        public int getTotalTimeMean() {
            return totalTimeMean;
        }

        public String getCommandKey() {
            return commandKey;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public long getTotalCount() {
            return totalCount;
        }

        public int getErrorPercentage() {
            return errorPercentage;
        }

        public int getExecutionTimeMean() {
            return executionTimeMean;
        }
    }
}
