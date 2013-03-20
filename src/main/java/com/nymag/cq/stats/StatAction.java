package com.nymag.cq.stats;

import org.apache.jackrabbit.core.id.NodeId;

import java.util.Map;

public interface StatAction {

    public void logChain();

    public void logStat();

    public void doChain(NodeId id, Actions action, Map<String, Object> properties);

    public void handle(NodeId id, Actions action, Map<String, Object> properties);
}
