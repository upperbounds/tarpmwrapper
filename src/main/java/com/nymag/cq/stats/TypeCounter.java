package com.nymag.cq.stats;

import com.day.crx.persistence.tar.TarPersistenceWrapper;
import org.apache.jackrabbit.core.id.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TypeCounter extends AbstractStatAction {
    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    long addedCount = 0;
    long loadedCount = 0;
    long storedCount = 0;
    public TypeCounter(StatAction action) {
        super(action);
    }

    @Override
    public void logStat() {
        log.info("#added: {}; #loaded: {}, #stored: " + storedCount , addedCount, loadedCount);
    }

    @Override
    public void handle(NodeId id, Actions action, Map<String, Object> properties) {
        switch (action){
            case NODE_ADDED:{
                addedCount++;
                break;
            }
            case NODE_LOADED:{
                loadedCount++;
                break;
            }
            case NODE_STORED:{
                storedCount++;
            }
        }
    }
}
