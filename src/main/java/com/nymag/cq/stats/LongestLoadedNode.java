package com.nymag.cq.stats;

import com.day.crx.persistence.tar.TarPersistenceWrapper;
import org.apache.jackrabbit.core.id.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class LongestLoadedNode extends AbstractStatAction {

    private long longest = 0l;
    private NodeId nodeId = null;
    private Date longestTime = new Date();
    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    public LongestLoadedNode(StatAction action) {
        super(action);
    }

    @Override
    public void logStat() {
        log.info("node {} has taken the longest to load at {} ms " + longestTime.toString(), nodeId, longest);
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
        }
    }
}
