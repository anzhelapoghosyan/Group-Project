import java.util.ArrayList;

public class Event {
    //instance variables
    private String title;
    private String date;
    private String location;
    private static Configuration configuration;
    
    public static void setConfiguration(Configuration config) {
        configuration = config;
    }
    //no-arg constructor
    public Event() {
        title = "";
        date = "";
        location = "";
    }
    //constructor
    public Event(String title, String date, String location) {
        this.title = title;
        this.date = date;
        this.location = location;
        if (configuration != null) {
            configuration.initialise(this);
        }
    }
    //copy constructor
    public Event(Event that) {
        this.title = that.title;
        this.date = that.date;
        this.location = that.location;
    }
    //title accessor
    public String getTitle() {
        return title;
    }
    //date accessor
    public String getDate() {
        return date;
    }
    //location accessor
    public String getLocation() {
        return location;
    }
    //title mutator
    public void setTitle(String newTitle) {
        title = newTitle;
    }
    //date mutator (should be checked by isOverlapping() before execution)
    public void setDate(String newDate) {
        date = newDate;
    }
    //location mutator
    public void setLocation(String newLocation) {
        location = newLocation;
    }
    //method for checking if the new Event object is overlapping with all the others in the schedule
    public boolean isOverlapping(ArrayList<Event> events) {
    if (events == null || date == null) 
        return false;

    String[] thisParts = date.split(" ");
    String thisStart = thisParts[0] + " " + thisParts[1];
    String thisEnd = thisParts[2] + " " + thisParts[3];
    for (int i = 0; i < events.size(); i++) {
        Event current = events.get(i);
        if (current == null || current.date == null) 
            continue;

        String[] currentParts = current.date.split(" ");
        String currentStart = currentParts[0] + " " + currentParts[1];
        String currentEnd = currentParts[2] + " " + currentParts[3];       
        if (thisStart.compareTo(currentEnd) < 0 && thisEnd.compareTo(currentStart) > 0) {
            return true;
        }
    }
    return false;
}
