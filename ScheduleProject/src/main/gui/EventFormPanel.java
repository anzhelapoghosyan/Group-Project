package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import model.Schedule;
import model.Event;
import model.RecurringEvent;

/**
 * The <code>EventFormPanel</code> class provides a GUI panel with a form to create 
 * and configure new events or recurring events.
 */
public class EventFormPanel extends JPanel {
    /** The schedule containing events to manage */
    private final Schedule schedule;
    /** The panel displaying events in table format */
    private final EventTablePanel eventTablePanel;
    /** Field for entering event title */
    private JTextField titleField;
    /** Field for entering event location */
    private JTextField locationField;
    /** Spinner for selecting start date */
    private JSpinner startDateSpinner;
    /** Spinner for selecting end date */ 
    private JSpinner endDateSpinner;
    /** Spinner for selecting start time */
    private JSpinner startTimeSpinner;
    /** Spinner for selecting end time */
    private JSpinner endTimeSpinner;

    /**
     * Constructs an EventFormPanel with the specified schedule and table panel.
     *
     * @param schedule the schedule to manage events for
     * @param eventTablePanel the table panel to refresh after changes
     */
    public EventFormPanel(Schedule schedule, EventTablePanel eventTablePanel) {
        this.schedule = schedule;
        this.eventTablePanel = eventTablePanel;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Add New Event"));
        initializeComponents();
    }

    /**
     * Initializes all UI components of the form.
     */
    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        titleField = createStyledTextField();
        locationField = createStyledTextField();
        
        
        SpinnerDateModel startDateModel = new SpinnerDateModel();
        SpinnerDateModel endDateModel = new SpinnerDateModel();
        startDateSpinner = new JSpinner(startDateModel);
        endDateSpinner = new JSpinner(endDateModel);
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "MMM d, yyyy");
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "MMM d, yyyy");
        startDateSpinner.setEditor(startDateEditor);
        endDateSpinner.setEditor(endDateEditor);

        
        SpinnerDateModel startTimeModel = new SpinnerDateModel();
        SpinnerDateModel endTimeModel = new SpinnerDateModel();
        startTimeSpinner = new JSpinner(startTimeModel);
        endTimeSpinner = new JSpinner(endTimeModel);
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "h:mm a");
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "h:mm a");
        startTimeSpinner.setEditor(startTimeEditor);
        endTimeSpinner.setEditor(endTimeEditor);

        
        styleSpinner(startDateSpinner);
        styleSpinner(endDateSpinner);
        styleSpinner(startTimeSpinner);
        styleSpinner(endTimeSpinner);

        
        gbc.gridx = 0; gbc.gridy = 0;
        add(createStyledLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        add(createStyledLabel("Location:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        add(createStyledLabel("Start Date:"), gbc);
        gbc.gridx = 1;
        add(startDateSpinner, gbc);
        gbc.gridx = 2;
        add(createStyledLabel("Start Time:"), gbc);
        gbc.gridx = 3;
        add(startTimeSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(createStyledLabel("End Date:"), gbc);
        gbc.gridx = 1;
        add(endDateSpinner, gbc);
        gbc.gridx = 2;
        add(createStyledLabel("End Time:"), gbc);
        gbc.gridx = 3;
        add(endTimeSpinner, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton addButton = createStyledButton("Add Event");
        JButton addRecurringButton = createStyledButton("Add Recurring Event");
        JButton removeButton = createStyledButton("Remove Event");

        buttonPanel.add(addButton);
        buttonPanel.add(addRecurringButton);
        buttonPanel.add(removeButton);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        add(buttonPanel, gbc);

        
        addButton.addActionListener(e -> addEvent());
        addRecurringButton.addActionListener(e -> showRecurringEventDialog());
        removeButton.addActionListener(e -> removeSelectedEvent());
    }

    /**
     * Creates a styled text field with consistent dimensions.
     *
     * @return the configured text field
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 25));
        return field;
    }

    /**
     * Creates a styled label with bold font.
     *
     * @param text the label text
     * @return the configured label
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setForeground(Color.BLACK);
        return label;
    }

    /**
     * Creates a styled button with hover effects.
     *
     * @param text the button text
     * @return the configured button
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(MainFrame.SOFT_PINK);
        button.setForeground(Color.BLACK);
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
             * Changes button color when mouse enters.
             *
             * @param e the mouse event
             */
            public void mouseEntered(MouseEvent e) {
                button.setBackground(MainFrame.DARKER_PINK);
            }

            /**
             * Restores button color when mouse exits.
             *
             * @param e the mouse event
             */
            public void mouseExited(MouseEvent e) {
                button.setBackground(MainFrame.SOFT_PINK);
            }
        });
        
        return button;
    }

    /**
     * Applies consistent styling to a spinner component.
     *
     * @param spinner the spinner to style
     */
    private void styleSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(120, 25));
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Adds a new event based on form inputs.
     */
    private void addEvent() {
        if (validateForm()) {
            LocalDateTime start = LocalDateTime.of(
                ((Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                ((Date) startTimeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
            );
            LocalDateTime end = LocalDateTime.of(
                ((Date) endDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                ((Date) endTimeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
            );

            Event event = new Event(titleField.getText(), start, end, locationField.getText());
            if (schedule.addEvent(event)) {
                eventTablePanel.refreshTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Event overlaps with existing events!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Shows a dialog for creating recurring events.
     */
    private void showRecurringEventDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add Recurring Event", true);
        dialog.setLayout(new BorderLayout());

        JPanel checkBoxPanel = new JPanel(new GridLayout(7, 1));
        JCheckBox[] dayCheckboxes = new JCheckBox[7];
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        for (int i = 0; i < 7; i++) {
            dayCheckboxes[i] = new JCheckBox(days[i]);
            checkBoxPanel.add(dayCheckboxes[i]);
        }

        JPanel buttonPanel = new JPanel();
        JButton okButton = createStyledButton("OK");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(checkBoxPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            dialog.dispose();
            addRecurringEvent(dayCheckboxes);
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Adds recurring events based on selected days.
     *
     * @param dayCheckboxes the array of day selection checkboxes
     */
    private void addRecurringEvent(JCheckBox[] dayCheckboxes) {
        if (validateForm()) {
            ArrayList<DayOfWeek> selectedDays = new ArrayList<>();
            for (int i = 0; i < dayCheckboxes.length; i++) {
                if (dayCheckboxes[i].isSelected()) {
                    selectedDays.add(DayOfWeek.values()[i]);
                }
            }

            if (selectedDays.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one day!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime start = LocalDateTime.of(
                ((Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                ((Date) startTimeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
            );
            LocalDateTime end = LocalDateTime.of(
                ((Date) endDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                ((Date) endTimeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
            );

            RecurringEvent recurringEvent = new RecurringEvent(
                titleField.getText(),
                start,
                end,
                locationField.getText(),
                selectedDays.toArray(new DayOfWeek[0]),
                start.toLocalDate(),
                end.toLocalDate()
            );

            for (Event event : recurringEvent.generateOccurrences()) {
                if (schedule.addEvent(event)) {
                    eventTablePanel.refreshTable();
                }
            }
            clearForm();
        }
    }

    /**
     * Removes the currently selected event from the table.
     */
    private void removeSelectedEvent() {
        int selectedRow = eventTablePanel.getEventTable().getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) eventTablePanel.getEventTable().getValueAt(selectedRow, 0);
            schedule.removeEvent(title);
            eventTablePanel.refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event to remove!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Validates that all required form fields are filled.
     *
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateForm() {
        if (titleField.getText().isEmpty() || locationField.getText().isEmpty() ||
            startDateSpinner.getValue() == null || endDateSpinner.getValue() == null ||
            startTimeSpinner.getValue() == null || endTimeSpinner.getValue() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Clears all form fields and resets them to default values.
     */
    private void clearForm() {
        titleField.setText("");
        locationField.setText("");
        startDateSpinner.setValue(new Date());
        endDateSpinner.setValue(new Date());
        startTimeSpinner.setValue(new Date());
        endTimeSpinner.setValue(new Date());
    }
}