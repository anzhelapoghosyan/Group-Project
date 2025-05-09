package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.DayOfWeek;
import model.Event;

/**
 * Dialog that displays a visual weekly calendar of scheduled events.
 * Provides interactive time slots and event management capabilities.
 */
public class WeeklyViewDialog extends JDialog {
    private static final Color HEADER_BACKGROUND = MainFrame.SOFT_PINK;
    private static final Color HEADER_FOREGROUND = Color.BLACK;
    /** Reference to the main application frame */
    private final MainFrame mainFrame;

    /**
     * Constructs the weekly view dialog.
     * @param mainFrame the parent MainFrame instance
     */
    public WeeklyViewDialog(MainFrame mainFrame) {
        super(mainFrame, "Weekly View", true);
        this.mainFrame = mainFrame;
        setSize(1200, 800);
        setLocationRelativeTo(mainFrame);
        initializeComponents();
    }

    /**
     * Initializes all components of the dialog.
     */
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel calendarPanel = createCalendarPanel();
        JScrollPane scrollPane = new JScrollPane(calendarPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Creates the calendar panel with time slots and events.
     * @return the configured calendar panel
     */
    private JPanel createCalendarPanel() {
        JPanel calendarPanel = new JPanel(new GridLayout(0, 8, 2, 2));
        calendarPanel.setBackground(Color.WHITE);

        
        calendarPanel.add(createTimeLabel());

        
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            calendarPanel.add(createDayLabel(weekStart.plusDays(i)));
        }

        
        for (int hour = 0; hour < 24; hour++) {
            calendarPanel.add(createHourLabel(hour));
            for (int day = 0; day < 7; day++) {
                calendarPanel.add(createTimeSlot(weekStart.plusDays(day), hour));
            }
        }

        return calendarPanel;
    }

    /**
     * Creates a time label for the calendar header.
     * @return a configured JLabel instance
     */
    private JLabel createTimeLabel() {
        JLabel label = new JLabel("Time");
        styleHeaderLabel(label);
        return label;
    }

    /**
     * Creates a day label for the calendar header.
     * @param date the date to display
     * @return a configured JLabel instance
     */
    private JLabel createDayLabel(LocalDate date) {
        JLabel label = new JLabel(String.format("<html>%s<br>%s</html>", 
            date.getDayOfWeek(),
            date.format(java.time.format.DateTimeFormatter.ofPattern("MMM d"))));
        styleHeaderLabel(label);
        return label;
    }

    /**
     * Creates an hour label for the time column.
     * @param hour the hour to display (0-23)
     * @return a configured JLabel instance
     */
    private JLabel createHourLabel(int hour) {
        JLabel label = new JLabel(String.format("%02d:00", hour));
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return label;
    }

    /**
     * Creates a time slot panel for a specific date and hour.
     * @param date the date for the time slot
     * @param hour the hour for the time slot
     * @return a configured JPanel instance
     */
    private JPanel createTimeSlot(LocalDate date, int hour) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        cell.setBackground(Color.WHITE);
        
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(Color.WHITE);
        
        for (Event event : mainFrame.getCurrentSchedule().getEvents()) {
            if (event.getStart().toLocalDate().equals(date) && 
                event.getStart().getHour() == hour) {
                eventPanel.add(createEventLabel(event));
            }
        }
        
        cell.add(eventPanel, BorderLayout.CENTER);
        return cell;
    }

    /**
     * Creates an event label for display in time slots.
     * @param event the event to display
     * @return a configured JLabel instance
     */
    private JLabel createEventLabel(Event event) {
        JLabel label = new JLabel(String.format("<html>%s<br>%s</html>", 
            event.getTitle(),
            event.getLocation()));
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }

    /**
     * Applies consistent styling to header labels.
     * @param label the label to style
     */
    private void styleHeaderLabel(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBackground(HEADER_BACKGROUND);
        label.setForeground(HEADER_FOREGROUND);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
    }

    /**
     * Creates the control panel with action buttons.
     * @return a configured JPanel instance
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Save Weekly Schedule");
        JButton closeButton = createStyledButton("Close");
        
        saveButton.addActionListener(e -> {
            mainFrame.getScheduleManager().saveSchedule();
            dispose();
        });
        closeButton.addActionListener(e -> dispose());
        
        controlPanel.add(saveButton);
        controlPanel.add(closeButton);
        return controlPanel;
    }

    /**
     * Creates a styled button with consistent appearance.
     * @param text the button text
     * @return a configured JButton instance
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(HEADER_BACKGROUND);
        button.setForeground(HEADER_FOREGROUND);
        button.setFocusPainted(false);
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.DARKER_PINK, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            /**
             * Handles mouse enter events for button hover effect.
             * @param e the mouse event
             */
            public void mouseEntered(MouseEvent e) {
                button.setBackground(MainFrame.DARKER_PINK);
            }

            /**
             * Handles mouse exit events for button hover effect.
             * @param e the mouse event
             */
            public void mouseExited(MouseEvent e) {
                button.setBackground(HEADER_BACKGROUND);
            }
        });
        
        return button;
    }
}