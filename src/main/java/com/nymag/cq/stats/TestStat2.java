package com.nymag.cq.stats;

import com.day.crx.persistence.tar.TarPersistenceWrapper;
import org.apache.jackrabbit.core.id.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestStat2 extends AbstractStatAction {
    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    NodeId nodeId;

    public TestStat2(StatAction action) {
        super(action);
    }

    @Override
    public void logStat() {
        log.info("im teststat2: last seen was {}", nodeId);
    }

    @Override
    public void handle(NodeId id, Actions action, Map<String, Object> properties) {
        this.nodeId = id;

    }
}
