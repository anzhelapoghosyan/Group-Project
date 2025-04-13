import java.time.LocalDateTime;
import java.util.ArrayList;

public class Event {
    private String title;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;

    // No-arg constructor
    public Event() {
        this.title = "";
        this.location = "";
        this.start = null;
        this.end = null;
    }

    // Constructor
    public Event(String title, LocalDateTime start, LocalDateTime end, String location) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    // Copy constructor
    public Event(Event that) {
        this.title = that.title;
        this.start = that.start;
        this.end = that.end;
        this.location = that.location;
    }

    // Accessors
    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    // Mutators
    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    // Check for overlapping event time
    public boolean isOverlapping(ArrayList<Event> events) {
        if (start == null || end == null || events == null) 
            return false;

        for (int i = 0; i < events.size(); i++) {
            Event current = events.get(i);
    
            if (current == null || current.start == null || current.end == null) 
                continue;
    
            // Check for time overlap
            if (start.isBefore(current.end) && end.isAfter(current.start)) {
                return true;
            }
        }
        return false;
    }

    // Format how the event prints
    public String toString() {
        return title + " at " + location + " from " + start + " to " + end;
    }
}