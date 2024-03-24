package com.crio.eventmanagement.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crio.eventmanagement.controller.exchange.requests.AddEventRequest;
import com.crio.eventmanagement.controller.exchange.requests.UpdateEventRequest;
import com.crio.eventmanagement.exception.EventDoesNotExistException;
import com.crio.eventmanagement.exception.ServerException;
import com.crio.eventmanagement.model.Event;
import com.crio.eventmanagement.repository.EventRepository;
import com.crio.eventmanagement.service.EventService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public Event addEvent(AddEventRequest addEventRequest) {
        addEventRequest.setRegisteredUsers(new ArrayList<>());
        Event event = Event.builder()
                .eventName(addEventRequest.getEventName())
                .eventDescription(addEventRequest.getEventDescription())
                .eventDate(addEventRequest.getEventDate())
                .eventTime(addEventRequest.getEventTime())
                .registeredUsers(addEventRequest.getRegisteredUsers())
                .build();

        if (event != null) {
            return eventRepository.save(event);
        }
        throw new ServerException("Server Error");
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(String eventId, UpdateEventRequest updateEventRequest) {
        Event existingEvent = eventRepository.findById(eventId).orElseThrow(() -> new EventDoesNotExistException("Event does not exist"));

        // Update the fields of the existing event with non-null values from the
        // provided event
        if (updateEventRequest.getEventName() != null) {
            existingEvent.setEventName(updateEventRequest.getEventName());
        }
        if (updateEventRequest.getEventDescription() != null) {
            existingEvent.setEventDescription(updateEventRequest.getEventDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            existingEvent.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getEventTime() != null) {
            existingEvent.setEventTime(updateEventRequest.getEventTime());
        }

        // Save the updated event to the database
        return eventRepository.save(existingEvent);
    }

    @Override
    public void deleteEvent(String eventId) {
        if(eventId == null){
            throw new EventDoesNotExistException("Event does not exist");
        }
        eventRepository.findById(eventId).orElseThrow(() -> new EventDoesNotExistException("Event does not exist"));
        eventRepository.deleteById(eventId);
    }
}