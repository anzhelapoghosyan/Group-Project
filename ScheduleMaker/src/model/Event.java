package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents a basic calendar event with a title, location, start, and end time.
 */
public class Event {
    /** The title of the event. */
    private String title;
    /** The location of the event. */
    private String location;
    /** The start date and time of the event. */
    private LocalDateTime start;
    /** The end date and time of the event. */
    private LocalDateTime end;
    /** Formatter for displaying dates in "MMM d, yyyy" format. */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");
    /** Formatter for displaying times in "h:mm a" format. */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a");

    /**
     * Default constructor initializes all fields to empty/null values.
     */
    public Event() {
        this.title = "";
        this.location = "";
        this.start = null;
        this.end = null;
    }

    /**
     * Constructs an event with specified parameters.
     *
     * @param title    the title of the event
     * @param start    the start time of the event
     * @param end      the end time of the event
     * @param location the location of the event
     */
    public Event(String title, LocalDateTime start, LocalDateTime end, String location) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    /**
     * Copy constructor creates a new event with the same properties as the given event.
     *
     * @param that the event to copy
     */
    public Event(Event that) {
        this.title = that.title;
        this.start = that.start;
        this.end = that.end;
        this.location = that.location;
    }

    /**
     * Gets the title of the event.
     *
     * @return the title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the location of the event.
     *
     * @return the location of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the start time of the event.
     *
     * @return the start time of the event
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets the end time of the event.
     *
     * @return the end time of the event
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Gets the formatted start date string.
     *
     * @return the formatted start date, or empty string if start is null
     */
    public String getFormattedStartDate() {
        return start != null ? start.format(DATE_FORMAT) : "";
    }

    /**
     * Gets the formatted end date string.
     *
     * @return the formatted end date, or empty string if end is null
     */
    public String getFormattedEndDate() {
        return end != null ? end.format(DATE_FORMAT) : "";
    }

    /**
     * Gets the formatted start time string.
     *
     * @return the formatted start time, or empty string if start is null
     */
    public String getFormattedStartTime() {
        return start != null ? start.format(TIME_FORMAT) : "";
    }

    /**
     * Gets the formatted end time string.
     *
     * @return the formatted end time, or empty string if end is null
     */
    public String getFormattedEndTime() {
        return end != null ? end.format(TIME_FORMAT) : "";
    }

    /**
     * Gets a formatted string representing the time range of the event.
     *
     * @return the formatted time range, or empty string if start/end is null
     */
    public String getFormattedTimeRange() {
        if (start == null || end == null) return "";
        String dateStr = start.format(DATE_FORMAT);
        String startTimeStr = start.format(TIME_FORMAT);
        String endTimeStr = end.format(TIME_FORMAT);
        return dateStr + " " + startTimeStr + " - " + endTimeStr;
    }

    /**
     * Sets the title of the event.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the location of the event.
     *
     * @param location the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the start time of the event.
     *
     * @param start the new start time
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Sets the end time of the event.
     *
     * @param end the new end time
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Checks if this event overlaps with any events in the given list.
     *
     * @param events the list of events to check against
     * @return true if there is an overlap, false otherwise
     */
    public boolean isOverlapping(ArrayList<Event> events) {
        if (start == null || end == null || events == null) return false;

        for (Event current : events) {
            if (current == null || current.start == null || current.end == null) continue;
            if (start.isBefore(current.end) && end.isAfter(current.start)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of the event.
     *
     * @return a formatted string with title, location, and time range
     */
    @Override
    public String toString() {
        return String.format("%s at %s\n%s", 
            title, 
            location, 
            getFormattedTimeRange());
    }
}
