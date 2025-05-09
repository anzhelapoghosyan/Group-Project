package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A collection of events representing a user's weekly schedule.
 */
public class Schedule {
    /** The name of the schedule. */
    private String name;
    /** The list of events in the schedule. */
    private ArrayList<Event> events;

    /**
     * Creates a schedule with the given name.
     *
     * @param name the name of the schedule
     */
    public Schedule(String name) {
        this.name = name;
        this.events = new ArrayList<>();
    }

    /**
     * Adds an event to the schedule if it does not overlap with existing events.
     *
     * @param newEvent the event to add
     * @return true if the event was added, false if it overlaps
     */
    public boolean addEvent(Event newEvent) {
        if (newEvent == null) return false;
        if (newEvent.isOverlapping(events)) {
            System.out.println("Event overlaps with an existing event.");
            return false;
        }
        events.add(newEvent);
        return true;
    }

    /**
     * Removes an event by its title (case-insensitive).
     *
     * @param title the title of the event to remove
     * @return true if the event was found and removed, false otherwise
     */
    public boolean removeEvent(String title) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getTitle().equalsIgnoreCase(title)) {
                events.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all events in the schedule.
     *
     * @return the list of events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Gets the name of the schedule.
     *
     * @return the schedule name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the schedule.
     *
     * @return a formatted string with all events
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Schedule: " + name + "\n\n");
        if (events.isEmpty()) {
            result.append("No events scheduled.\n");
        } else {
            for (int i = 0; i < events.size(); i++) {
                Event e = events.get(i);
                result.append(i + 1).append(". ").append(e.getTitle()).append("\n");
                result.append("   Location: ").append(e.getLocation()).append("\n");
                result.append("   Start: ").append(e.getStart()).append("\n");
                result.append("   End: ").append(e.getEnd()).append("\n\n");
            }
        }
        result.append("End of Schedule :)\n");
        return result.toString();
    }

    /**
     * Gets the start date of the week containing the given date.
     *
     * @param date any date within the week
     * @return the Monday of that week
     */
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }

    /**
     * Generates a weekly view of events for the week containing the given date.
     *
     * @param anyDate any date within the target week
     * @return a formatted string showing events grouped by day
     */
    public String toWeeklyView(LocalDate anyDate) {
        LocalDate startOfWeek = getStartOfWeek(anyDate);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d");
        StringBuilder output = new StringBuilder();

        output.append("Weekly Schedule (").append(startOfWeek).append(" - ").append(endOfWeek).append(")\n\n");
        for (int i = 0; i < 7; i++) {
            LocalDate current = startOfWeek.plusDays(i);
            output.append(formatter.format(current)).append(":\n");
            boolean hasEvents = false;

            for (Event e : events) {
                if (e.getStart().toLocalDate().equals(current)) {
                    output.append("  ").append(e).append("\n");
                    hasEvents = true;
                }
            }
            if (!hasEvents) {
                output.append("  No events.\n");
            }
            output.append("\n");
        }
        return output.toString();
    }
}