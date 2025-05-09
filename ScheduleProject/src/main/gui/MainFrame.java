package main.gui;

import javax.swing.*;
import java.awt.*;
import model.Schedule;
import model.ScheduleManager;

/**
 * The main application window that manages layout and coordination between GUI components.
 * Handles the event form, table display, and schedule management operations.
 */
public class MainFrame extends JFrame {
    /** Manager for schedule persistence operations */
    private ScheduleManager scheduleManager;
    /** The currently displayed schedule */
    private Schedule currentSchedule;
    /** Panel displaying events in table format */
    private EventTablePanel eventTablePanel;
    /** Panel containing event creation form */
    private EventFormPanel eventFormPanel;
    /** Soft pink color used for UI elements */
    public static final Color SOFT_PINK = new Color(222, 184, 184); 
    /** Darker pink color used for hover effects */ 
    public static final Color DARKER_PINK = new Color(199, 165, 165); 

    /**
     * Constructs the main application frame and initializes all components.
     */
    public MainFrame() {
        scheduleManager = new ScheduleManager();
        currentSchedule = new Schedule("Weekly Schedule");
        scheduleManager.addSchedule(currentSchedule);

        setTitle("Schedule Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeComponents();
        layoutComponents();
    }

    /**
     * Initializes all UI components of the application.
     */
    private void initializeComponents() {
        
        setJMenuBar(new MenuBar(this));

        
        eventTablePanel = new EventTablePanel(currentSchedule);
        eventFormPanel = new EventFormPanel(currentSchedule, eventTablePanel);
    }

    /**
     * Arranges the components in the main frame using appropriate layout managers.
     */
    private void layoutComponents() {
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        mainPanel.add(eventTablePanel, BorderLayout.CENTER);
        mainPanel.add(eventFormPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Returns the schedule manager instance.
     * @return the ScheduleManager instance
     */
    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    /**
     * Returns the currently displayed schedule.
     * @return the current Schedule instance
     */
    public Schedule getCurrentSchedule() {
        return currentSchedule;
    }

    /**
     * Entry point for the application.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}