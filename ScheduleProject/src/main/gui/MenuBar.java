package main.gui;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.ArrayList;
import model.Event;

/**
 * Custom menu bar providing options for saving, exporting, and switching views.
 * Handles schedule persistence and view management operations.
 */
public class MenuBar extends JMenuBar {
    /** Reference to the main application frame */
    private final MainFrame mainFrame;
    private static final Color HEADER_BACKGROUND = MainFrame.SOFT_PINK;
    private static final Color HEADER_FOREGROUND = Color.BLACK;

    /**
     * Constructs the menu bar with reference to the main frame.
     * @param mainFrame the parent MainFrame instance
     */
    public MenuBar(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(HEADER_BACKGROUND);
        setOpaque(true);
        
        createFileMenu();
        createViewMenu();
    }

    /**
     * Creates and configures the File menu with its items.
     */
    private void createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(HEADER_FOREGROUND);
        
        JMenuItem saveItem = new JMenuItem("Save Schedule");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        saveItem.addActionListener(e -> mainFrame.getScheduleManager().saveSchedule());
        exitItem.addActionListener(e -> System.exit(0));

        add(fileMenu);
    }

    /**
     * Creates and configures the View menu with its items.
     */
    private void createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setForeground(HEADER_FOREGROUND);
        
        JMenuItem weeklyViewItem = new JMenuItem("Weekly View");
        viewMenu.add(weeklyViewItem);

        weeklyViewItem.addActionListener(e -> new WeeklyViewDialog(mainFrame));

        add(viewMenu);
    }

    /**
     * Saves the current schedule by generating HTML files for each week.
     */
    private void saveSchedule() {
        try {
            
            java.io.File scheduleDir = new java.io.File("weekly_schedules");
            if (!scheduleDir.exists()) {
                scheduleDir.mkdir();
            }

            
            List<Event> allEvents = mainFrame.getCurrentSchedule().getEvents();
            
            
            Map<LocalDate, List<Event>> weeklyEvents = new TreeMap<>();
            
            for (Event event : allEvents) {
                LocalDate eventDate = event.getStart().toLocalDate();
                LocalDate weekStart = eventDate.with(java.time.DayOfWeek.MONDAY);
                weeklyEvents.computeIfAbsent(weekStart, k -> new ArrayList<>()).add(event);
            }

            
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

            JOptionPane.showMessageDialog(mainFrame, 
                "Weekly schedules have been generated in the 'weekly_schedules' directory!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Failed to save schedules: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generates HTML content for a weekly schedule view.
     * @param weekStart the starting date of the week
     * @param events the list of events for the week
     * @return the generated HTML as a String
     */
    private String generateWeeklyHtml(LocalDate weekStart, List<Event> events) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<style>\n");
        html.append("table { border-collapse: collapse; width: 100%; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("th { background-color: #9370DB; color: black; }\n");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("</style>\n</head>\n<body>\n");
        
        html.append("<h2>Weekly Schedule: ")
           .append(weekStart.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
           .append(" to ")
           .append(weekStart.plusDays(6).format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
           .append("</h2>\n");

        
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = weekStart.plusDays(i);
            html.append("<h3>").append(currentDate.getDayOfWeek()).append(" - ")
                .append(currentDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
                .append("</h3>\n");
            
            html.append("<table>\n");
            html.append("<tr><th>Time</th><th>Event</th><th>Location</th></tr>\n");
            
            
            List<Event> dayEvents = events.stream()
                .filter(e -> e.getStart().toLocalDate().equals(currentDate))
                .sorted(Comparator.comparing(Event::getStart))
                .collect(Collectors.toList());
            
            for (Event event : dayEvents) {
                html.append("<tr>\n");
                html.append("<td>")
                    .append(event.getStart().format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")))
                    .append(" - ")
                    .append(event.getEnd().format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")))
                    .append("</td>\n");
                html.append("<td>").append(event.getTitle()).append("</td>\n");
                html.append("<td>").append(event.getLocation()).append("</td>\n");
                html.append("</tr>\n");
            }
            
            html.append("</table><br>\n");
        }
        
        html.append("</body>\n</html>");
        return html.toString();
    }

    /**
     * Displays the weekly view dialog.
     */
    private void showWeeklyView() {
        new WeeklyViewDialog(mainFrame);
    }
}