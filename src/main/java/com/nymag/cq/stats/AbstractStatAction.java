package com.nymag.cq.stats;

import org.apache.jackrabbit.core.id.NodeId;

import java.util.Map;

public abstract class AbstractStatAction implements StatAction{

    private StatAction nextAction;

    public AbstractStatAction(StatAction action){
        this.nextAction = action;
    }
    
    @Override
    public void doChain(NodeId id, Actions action, Map<String, Object> properties){
        this.handle(id, action, properties);
        if(null != nextAction){
            nextAction.doChain(id, action, properties);
        }
    }

    @Override
    public void logChain(){
        this.logStat();
        if(null != nextAction){
            nextAction.logChain();
        }
    }
}
