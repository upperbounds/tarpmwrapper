package com.day.crx.persistence.tar;

import org.apache.jackrabbit.core.id.NodeId;
import org.apache.jackrabbit.core.id.PropertyId;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.PersistenceManager;
import org.apache.jackrabbit.core.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TarPersistenceWrapper implements PersistenceManager {

    private TarPersistenceManager tarPM;

    private static Logger log = LoggerFactory.getLogger(TarPersistenceWrapper.class);

    @Override
    public void init(PMContext context) throws Exception {
        tarPM = new TarPersistenceManager();
        tarPM.init(context);
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
    public void store(ChangeLog changeLog) throws ItemStateException {
        log.info("store: {}", changeLog);
        tarPM.store(changeLog);
    }

    @Override
    public void checkConsistency(String[] uuids, boolean recursive, boolean fix) {
        tarPM.checkConsistency(uuids, recursive, fix);
    }

    @Override
    public synchronized void close() throws Exception {
        tarPM.close();
    }

    @Override
    public NodeState createNew(NodeId id) {
        log.info("createNewNode: {}", id);
        return tarPM.createNew(id);
    }

    @Override
    public PropertyState createNew(PropertyId id) {
        log.info("createNewProperty: {}", id);
        return tarPM.createNew(id);
    }

    @Override
    public NodeState load(NodeId id) throws NoSuchItemStateException, ItemStateException {
        log.info("loadNode: {}", id);
        return tarPM.load(id);
    }

    @Override
    public PropertyState load(PropertyId id) throws NoSuchItemStateException, ItemStateException {
        log.info("loadProperty: {}", id);
        return tarPM.load(id);
    }
}
