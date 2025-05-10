package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import model.Schedule;
import model.Event;

/**
 * The <code>EventTablePanel</code> class displays a table view of all events 
 * in the current schedule using a JTable with custom styling.
 */
public class EventTablePanel extends JPanel {
    /** The schedule containing events to display */
    private final Schedule schedule;
    /** The table displaying events */
    private JTable eventTable;
    /** The table model backing the event table */
    private DefaultTableModel tableModel;
    private static final String[] COLUMN_NAMES = {"Title", "Location", "Date", "Time"};
    private static final int[] COLUMN_WIDTHS = {300, 250, 120, 150};
    private static final Color HEADER_BACKGROUND = MainFrame.SOFT_PINK;
    private static final Color ALTERNATE_ROW_COLOR = new Color(245, 240, 240);

    /**
     * Constructs an EventTablePanel for the specified schedule.
     *
     * @param schedule the schedule containing events to display
     */
    public EventTablePanel(Schedule schedule) {
        this.schedule = schedule;
        setLayout(new BorderLayout());
        createTable();
        add(new JScrollPane(eventTable), BorderLayout.CENTER);
        refreshTable();
    }

    /**
     * Initializes and configures the event table with custom styling.
     */
    private void createTable() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            /**
             * Makes table cells non-editable.
             *
             * @param row the row index
             * @param column the column index
             * @return false to prevent cell editing
             */
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventTable = new JTable(tableModel);
        eventTable.setRowHeight(25);
        eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = eventTable.getColumnModel();
        for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(COLUMN_WIDTHS[i]);
        }

        // Create a custom cell renderer that wraps text
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setText("<html><div style='width:" + table.getColumnModel().getColumn(column).getWidth() + "px;'>" + value + "</div></html>");
                }
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : ALTERNATE_ROW_COLOR);
                }
                return c;
            }
        };

        for (int i = 0; i < eventTable.getColumnCount(); i++) {
            eventTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        eventTable.getTableHeader().setBackground(HEADER_BACKGROUND);
        eventTable.getTableHeader().setForeground(Color.BLACK);
        eventTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        eventTable.setShowGrid(true);
        eventTable.setGridColor(Color.LIGHT_GRAY);
        eventTable.setSelectionBackground(ALTERNATE_ROW_COLOR);
        eventTable.setSelectionForeground(Color.BLACK);

        eventTable.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    /**
     * Refreshes the table with current events from the schedule.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Event event : schedule.getEvents()) {
            tableModel.addRow(new Object[]{
                    event.getTitle(),
                    event.getLocation(),
                    event.getFormattedStartDate(),
                    event.getFormattedStartTime() + " - " + event.getFormattedEndTime()
            });
        }
    }

    /**
     * Returns the event table component.
     *
     * @return the JTable displaying events
     */
    public JTable getEventTable() {
        return eventTable;
    }
}
