import java.util.ArrayList;

public class Report {

    public static double calculateTotal(ArrayList<Double> consumption) {
        double total = 0;

        for (double value : consumption) {
            total += value;
        }

        return total;
    }

    public static double calculateAverage(ArrayList<Double> consumption) {
        if (consumption.isEmpty()) {
            return 0;
        }

        return calculateTotal(consumption) / consumption.size();
    }

    public static int countAbnormalEvents(
            ArrayList<Double> consumption,
            double averageUsage) {
        double threshold = averageUsage * 1.5;
        int count = 0;

        for (double value : consumption) {
            if (value > threshold) {
                count++;
            }
        }

        return count;
    }
}
