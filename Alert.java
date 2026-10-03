public class Alert {
    public static String checkAlert(double currentUsage, double averageUsage) {
        double threshold = averageUsage * 1.5;
        if (currentUsage > threshold) {
            return "⚠ ABNORMAL CONSUMPTION DETECTED\n\n"
                    + "Possible leakage, spillage,\n"
                    + "or unusually high usage.\n\n"
                    + "Please inspect the system.\n"
                    + "This alert indicates a possible issue and does not physically confirm a leak.";
        }
        return "SYSTEM STATUS: NORMAL";
    }
}
