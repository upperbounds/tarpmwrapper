package com.nymag.cq.stats;

import com.day.crx.persistence.tar.TarPersistenceWrapper;
import org.apache.jackrabbit.core.id.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LongestLoadedNode extends AbstractStatAction {

    private long longest = 0l;
    private NodeId nodeId = null;
    private Date longestTime = new Date();
    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    private List<Long> loadTimes = new ArrayList<Long>();

    public LongestLoadedNode(StatAction action) {
        super(action);
    }

    @Override
    public void logStat() {
        log.info("node {} has taken the longest to since the last reporting time: {} ms at " + longestTime.toString(), nodeId, longest);

        synchronized (loadTimes) {
            long average = 0;
            for (Long tm : loadTimes) {
                average += tm;
            }
            if (loadTimes.size() > 0) {
                log.info("average load time was {} for the last reporting period", average / ((float) loadTimes.size()));
            }
        }

        synchronized (this) {
            longest = -1l;
            nodeId = null;
            longestTime = new Date();
            loadTimes = new ArrayList<Long>();
        }
    }

    @Override
    public void handle(NodeId id, Actions action, Map<String, Object> properties) {

        if (action == Actions.NODE_LOADED) {
            long loadTime = (Long) properties.get("loadTime");

            if (loadTime > longest) {
                longest = loadTime;
                nodeId = id;
                longestTime = new Date();
            }
            loadTimes.add(loadTime);
        }
    }
}
