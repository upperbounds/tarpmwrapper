package com.day.crx.persistence.tar;

import com.nymag.cq.stats.*;
import org.apache.jackrabbit.core.id.NodeId;
import org.apache.jackrabbit.core.id.PropertyId;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.PersistenceManager;
import org.apache.jackrabbit.core.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TarPersistenceWrapper implements PersistenceManager {

    private TarPersistenceManager tarPM;

    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    private int reportInterval = 1000 * 60 * 10;

    StatAction actionChain;

    Thread actionLogger;

    @Override
    public void init(PMContext context) throws Exception {
        tarPM = new TarPersistenceManager();
        tarPM.init(context);
        log.info("TarPersistenceManager initialized");
        actionChain = new TypeCounter(new LongestCreatedNode(new LongestLoadedNode(null)));

        actionLogger = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        actionLogger.sleep(reportInterval);
                        actionChain.logChain();

                    } catch (InterruptedException e) {
                        log.info("shutting down stat logging thread");
                        return;
                    }  catch (RuntimeException re){
                        // prevents thread from death
                        log.info("Exception caught while running logging loop {}", re);
                    }

                }
            }
        });

        actionLogger.start();
        log.info("wrapper initialized");
    }

    public void setReportInterval(String val){
        reportInterval = Integer.valueOf(val);
    }
    @Override
    public NodeReferences loadReferencesTo(NodeId id) throws NoSuchItemStateException, ItemStateException {
        return tarPM.loadReferencesTo(id);
    }

    @Override
    public boolean exists(NodeId id) throws ItemStateException {
        return tarPM.exists(id);
    }

    @Override
    public boolean exists(PropertyId id) throws ItemStateException {
        return tarPM.exists(id);
    }

    @Override
    public boolean existsReferencesTo(NodeId targetId) throws ItemStateException {
        return tarPM.existsReferencesTo(targetId);
    }

    @Override
    public synchronized void store(ChangeLog changeLog) throws ItemStateException {

        for (ItemState state : changeLog.addedStates()) {
            if (state.isNode()) {
                NodeId id = (NodeId) state.getId();
                actionChain.doChain(id, Actions.NODE_STORED, new HashMap<String, Object>());
            }
        }
        for (ItemState state : changeLog.modifiedStates()) {
            if (state.isNode()) {
                NodeId id = (NodeId) state.getId();
                actionChain.doChain(id, Actions.NODE_STORED, new HashMap<String, Object>());
            }

        }
        for (ItemState state : changeLog.deletedStates()) {
            if (state.isNode()) {
                NodeId id = (NodeId) state.getId();
                actionChain.doChain(id, Actions.NODE_STORED, new HashMap<String, Object>());
            }
        }

        tarPM.store(changeLog);
    }

    @Override
    public void checkConsistency(String[] uuids, boolean recursive, boolean fix) {
        tarPM.checkConsistency(uuids, recursive, fix);
    }

    @Override
    public synchronized void close() throws Exception {
        actionLogger.interrupt();
        tarPM.close();
    }

    @Override
    public NodeState createNew(NodeId id) {

        long start = System.currentTimeMillis();
        NodeState state = tarPM.createNew(id);
        long end = System.currentTimeMillis();
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("createTime", end - start);
        actionChain.doChain(state.getNodeId(), Actions.NODE_ADDED, props);
        return state;
    }

    @Override
    public PropertyState createNew(PropertyId id) {
        return tarPM.createNew(id);
    }

    @Override
    public synchronized NodeState load(NodeId id) throws NoSuchItemStateException, ItemStateException {
        long start = System.currentTimeMillis();
        NodeState state = tarPM.load(id);
        long end = System.currentTimeMillis();
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("loadTime", end - start);
        actionChain.doChain(id, Actions.NODE_LOADED, props);
        return state;

    }

    @Override
    public PropertyState load(PropertyId id) throws NoSuchItemStateException, ItemStateException {
        return tarPM.load(id);
    }
}
