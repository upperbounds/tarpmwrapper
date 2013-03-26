package com.nymag.cq.stats;

import com.day.crx.persistence.tar.TarPersistenceWrapper;
import org.apache.jackrabbit.core.id.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class LongestCreatedNode extends AbstractStatAction {

    private long longest = -1l;
    private NodeId nodeId = null;
    private Date longestTime = new Date();
    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    public LongestCreatedNode(StatAction action) {
        super(action);
    }

    @Override
    public void logStat() {
        log.info("node {} has taken the longest to create since the last reporting time: {} ms at " + longestTime.toString(), nodeId, longest);
        synchronized (this){
            longest = -1l;
            nodeId = null;
            longestTime = new Date();
        }
    }

    @Override
    public void handle(NodeId id, Actions action, Map<String, Object> properties) {

        if (action == Actions.NODE_ADDED) {
            long loadTime = (Long) properties.get("createTime");

            if (loadTime > longest) {
                longest = loadTime;
                nodeId = id;
                longestTime = new Date();
            }
        }
    }
}
