import java.util.ArrayList;
public class ScheduleManager {
  private ArrayList<Schedule> schList;

    public ScheduleManager() {
        schList = new ArrayList<>();
    }
  public void addSchedule(Schedule s) {
        schList.add(s);
    }
   public void removeSchedule(String name) {
        for (int i = 0; i < schList.size(); i++) {
            if (schList.get(i).getName().equalsIgnoreCase(name)) {
                schList.remove(i); 
              System.out.println("You removed the event:" + name);
            }
          else {
            System.out.println("There is no such event with the name:" + name);
          }
        }
   }
  public Schedule getSchedule(String name) {
    for (Schedule s : schList) {
        if (s.getName().equalsIgnoreCase(name)) {
            return s;
        }
    }
    return null;
}
   public void listSchedules() {
        if (schedules.isEmpty()) {
            System.out.println("No schedules available.");
        } else {
            System.out.println("Schedules:");
            for (Schedule s : schedules) {
                System.out.println("- " + s.getName());
            }
        }
    }
}

