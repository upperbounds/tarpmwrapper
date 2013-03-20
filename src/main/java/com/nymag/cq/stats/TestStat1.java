package com.nymag.cq.stats;

import com.day.crx.persistence.tar.TarPersistenceWrapper;
import org.apache.jackrabbit.core.id.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: colin
 * Date: 3/17/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestStat1 extends AbstractStatAction {
    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    long count = 0;
    public TestStat1(StatAction action) {
        super(action);
    }

    @Override
    public void logStat() {
        log.info("im teststat1 with count {}", count);
    }

    @Override
    public void handle(NodeId id, Actions action, Map<String, Object> properties) {
        count++;
    }
}
