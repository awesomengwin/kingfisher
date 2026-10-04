package com.awesomengwin.kingfisher.common;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {

    private final List<Object> domainEvents = new ArrayList<>();

    protected void registerEvent(Object event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    public List<Object> pullDomainEvents() {
        List<Object> snapshot = List.copyOf(domainEvents);
        domainEvents.clear();
        return snapshot;
    }
}
