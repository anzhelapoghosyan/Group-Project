public class Configuration {
    private String dateFormat;

    public Configuration(String format) {
        dateFormat = format;
    }

    public Configuration(Configuration that) {
        dateFormat = that.dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void initialise(Event2 event) {
        String[] parts = event.getDate().split(" ");
        if (parts.length != 4) {
            System.out.println("Invalid date format");
        }
    }
}
