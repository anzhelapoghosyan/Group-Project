package model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * The <code>ScheduleManager</code> class manages a schedule instance and handles persistence-related 
 * operations like saving and exporting schedules to HTML files.
 */
public class ScheduleManager {
    private Schedule currentSchedule;

    /**
     * Constructs a new ScheduleManager with a default weekly schedule.
     */
    public ScheduleManager() {
        currentSchedule = new Schedule("Weekly Schedule");
    }

    /**
     * Sets the current schedule to the specified schedule.
     *
     * @param schedule the schedule to set as current
     */
    public void addSchedule(Schedule schedule) {
        currentSchedule = schedule;
    }

    /**
     * Returns the current schedule being managed.
     *
     * @return the current schedule
     */
    public Schedule getCurrentSchedule() {
        return currentSchedule;
    }

    /**
     * Saves the current schedule by generating HTML files for each week.
     */
    public void saveSchedule() {
        try {
            createScheduleDirectory();
            List<Event> allEvents = currentSchedule.getEvents();
            Map<LocalDate, List<Event>> weeklyEvents = groupEventsByWeek(allEvents);
            generateWeeklyFiles(weeklyEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the directory for storing weekly schedules if it doesn't exist.
     */
    private void createScheduleDirectory() {
        java.io.File scheduleDir = new java.io.File("weekly_schedules");
        if (!scheduleDir.exists()) {
            scheduleDir.mkdir();
        }
    }

    /**
     * Groups events by their starting week (Monday to Sunday).
     *
     * @param events the list of events to group
     * @return a map of events grouped by week start date
     */
    private Map<LocalDate, List<Event>> groupEventsByWeek(List<Event> events) {
        Map<LocalDate, List<Event>> weeklyEvents = new TreeMap<>();
        for (Event event : events) {
            LocalDate eventDate = event.getStart().toLocalDate();
            LocalDate weekStart = eventDate.with(DayOfWeek.MONDAY);
            weeklyEvents.computeIfAbsent(weekStart, k -> new ArrayList<>()).add(event);
        }
        return weeklyEvents;
    }

    /**
     * Generates HTML files for each week of events.
     *
     * @param weeklyEvents the map of events grouped by week
     * @throws IOException if there's an error writing the files
     */
    private void generateWeeklyFiles(Map<LocalDate, List<Event>> weeklyEvents) throws IOException {
        for (Map.Entry<LocalDate, List<Event>> weekEntry : weeklyEvents.entrySet()) {
            LocalDate weekStart = weekEntry.getKey();
            LocalDate weekEnd = weekStart.plusDays(6);
            String fileName = String.format("weekly_schedules/schedule_%s_to_%s.html", 
                weekStart.format(java.time.format.DateTimeFormatter.ISO_DATE),
                weekEnd.format(java.time.format.DateTimeFormatter.ISO_DATE));

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(generateWeeklyHtml(weekStart, weekEntry.getValue()));
            }
        }
    }

    /**
     * Generates HTML content for a week's schedule.
     *
     * @param weekStart the start date of the week (Monday)
     * @param events the list of events for the week
     * @return the generated HTML as a string
     */
    private String generateWeeklyHtml(LocalDate weekStart, List<Event> events) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append(".schedule-container { max-width: 1200px; margin: 0 auto; }\n");
        html.append(".day-schedule { margin-bottom: 30px; }\n");
        html.append("h2 { color: #333; text-align: center; margin-bottom: 20px; }\n");
        html.append("h3 { color: #666; margin: 15px 0; }\n");
        html.append("table { border-collapse: collapse; width: 100%; table-layout: fixed; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
        html.append("th { background-color: #DEB8B8; color: black; font-weight: bold; }\n");
        html.append("tr:nth-child(even) { background-color: #f5f0f0; }\n");
        html.append(".time-column { width: 20%; }\n");
        html.append(".event-column { width: 50%; }\n");
        html.append(".location-column { width: 30%; }\n");
        html.append("td { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }\n");
        html.append("</style>\n</head>\n<body>\n");
        
        html.append("<div class='schedule-container'>\n");
        html.append("<h2>Weekly Schedule: ")
           .append(weekStart.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
           .append(" to ")
           .append(weekStart.plusDays(6).format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
           .append("</h2>\n");

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = weekStart.plusDays(i);
            html.append(generateDayTable(currentDate, events));
        }
        
        html.append("</div>\n");
        html.append("</body>\n</html>");
        return html.toString();
    }

    /**
     * Generates HTML table for a single day's events.
     *
     * @param date the date to generate the table for
     * @param events the list of all events (will be filtered by date)
     * @return the generated HTML table as a string
     */
    private String generateDayTable(LocalDate date, List<Event> events) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='day-schedule'>\n");
        html.append("<h3>").append(date.getDayOfWeek()).append(" - ")
            .append(date.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
            .append("</h3>\n");
        
        html.append("<table>\n");
        html.append("<tr>\n");
        html.append("<th class='time-column'>Time</th>\n");
        html.append("<th class='event-column'>Event</th>\n");
        html.append("<th class='location-column'>Location</th>\n");
        html.append("</tr>\n");
        
        List<Event> dayEvents = events.stream()
            .filter(e -> e.getStart().toLocalDate().equals(date))
            .sorted(Comparator.comparing(Event::getStart))
            .collect(Collectors.toList());
        
        if (dayEvents.isEmpty()) {
            html.append("<tr><td colspan='3' style='text-align: center;'>No events scheduled</td></tr>\n");
        } else {
            for (Event event : dayEvents) {
                html.append("<tr>\n");
                html.append("<td class='time-column'>")
                    .append(event.getStart().format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")))
                    .append(" - ")
                    .append(event.getEnd().format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")))
                    .append("</td>\n");
                html.append("<td class='event-column'>").append(event.getTitle()).append("</td>\n");
                html.append("<td class='location-column'>").append(event.getLocation()).append("</td>\n");
                html.append("</tr>\n");
            }
        }
        
        html.append("</table>\n");
        html.append("</div>\n");
        return html.toString();
    }
}