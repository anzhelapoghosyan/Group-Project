package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Extends Event to support repeating events on specific days between a start and end date.
 */
public class RecurringEvent extends Event {
    /** Days of the week when the event recurs. */
    private DayOfWeek[] daysOfWeek;
    /** Start date of the recurring event series. */
    private LocalDate startDate;
    /** End date of the recurring event series. */
    private LocalDate endDate;

    /**
     * Constructs a recurring event.
     *
     * @param title      the title of the event
     * @param startTime  the start time of each occurrence
     * @param endTime    the end time of each occurrence
     * @param location   the location of the event
     * @param daysOfWeek the days of the week when the event recurs
     * @param startDate  the start date of the recurring series
     * @param endDate    the end date of the recurring series
     */
    public RecurringEvent(String title, LocalDateTime startTime, LocalDateTime endTime, 
                         String location, DayOfWeek[] daysOfWeek, LocalDate startDate, LocalDate endDate) {
        super(title, startTime, endTime, location);
        this.daysOfWeek = daysOfWeek;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Generates all occurrences of this recurring event within the date range.
     *
     * @return a list of Event objects representing each occurrence
     */
    public ArrayList<Event> generateOccurrences() {
        ArrayList<Event> occurrences = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            for (DayOfWeek d : daysOfWeek) {
                if (current.getDayOfWeek() == d) {
                    LocalDateTime s = LocalDateTime.of(current, getStart().toLocalTime());
                    LocalDateTime e = LocalDateTime.of(current, getEnd().toLocalTime());
                    occurrences.add(new Event(getTitle(), s, e, getLocation()));
                }
            }
            current = current.plusDays(1);
        }
        return occurrences;
    }
}