package com.sunilson.bachelorthesis.domain.model;

import com.sunilson.bachelorthesis.presentation.shared.utilities.DateUtilities;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

public class DomainDay {

    public DomainDay(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Getter
    @Setter
    private LocalDate localDate;

    @Getter
    @Setter
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public void addEvent(DomainEvent event) {
        if(DateUtilities.isEventOverlappingDay(localDate.toDateTimeAtStartOfDay(), event.getFrom(), event.getTo())) {
            domainEvents.add(event);
        }

        Collections.sort(domainEvents, new Comparator<DomainEvent>() {
            @Override
            public int compare(DomainEvent e1, DomainEvent e2) {
                return e1.getFrom().compareTo(e2.getFrom());
            }
        });
    }
}
