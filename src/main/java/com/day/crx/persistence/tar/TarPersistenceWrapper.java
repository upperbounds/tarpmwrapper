package com.day.crx.persistence.tar;

import com.nymag.cq.stats.Actions;
import com.nymag.cq.stats.StatAction;
import com.nymag.cq.stats.TestStat1;
import com.nymag.cq.stats.TestStat2;
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

    StatAction actionChain;

    Thread actionLogger;

    @Override
    public void init(PMContext context) throws Exception {
        tarPM = new TarPersistenceManager();
        tarPM.init(context);
        log.info("TarPersistenceManager initialized");
        actionChain = new TestStat1(new TestStat2(null));

        actionLogger = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        actionLogger.sleep(1000 * 30);
                        actionChain.logChain();

                    } catch (InterruptedException e) {
                        log.info("shutting down stat logging thread");
                        return;
                    }
                }
            }
        });

        actionLogger.start();
        log.info("wrapper initialized");
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
        return tarPM.createNew(id);
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
