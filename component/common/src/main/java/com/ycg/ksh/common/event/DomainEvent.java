package com.ycg.ksh.common.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  领域事件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class DomainEvent implements Serializable {

    private Serializable eventId;

    private LocalDateTime occurrence;

    public DomainEvent() {
        occurrence = LocalDateTime.now();
    }

    public DomainEvent(Serializable eventId) {
        this();
        this.eventId = eventId;
    }

    public Serializable getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurrence() {
        return occurrence;
    }

    public void setEventId(Serializable eventId) {
        this.eventId = eventId;
    }

    public void setOccurrence(LocalDateTime occurrence) {
        this.occurrence = occurrence;
    }
}
