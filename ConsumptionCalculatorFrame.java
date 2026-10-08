import javax.swing.*;
import java.awt.*;

public class ConsumptionCalculatorFrame extends JFrame {

    JTextField day1, day2, day3, day4, day5;
    JTextField daily, average, minimum, maximum, variation;

    public ConsumptionCalculatorFrame() {

        setTitle("Consumption Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Daily input
        panel.add(new JLabel("Day 1 Consumption (L):"));
        day1 = new JTextField();
        panel.add(day1);

        panel.add(new JLabel("Day 2 Consumption (L):"));
        day2 = new JTextField();
        panel.add(day2);

        panel.add(new JLabel("Day 3 Consumption (L):"));
        day3 = new JTextField();
        panel.add(day3);

        panel.add(new JLabel("Day 4 Consumption (L):"));
        day4 = new JTextField();
        panel.add(day4);

        panel.add(new JLabel("Day 5 Consumption (L):"));
        day5 = new JTextField();
        panel.add(day5);

        // Calculate button
        JButton calculate = new JButton("Calculate");
        panel.add(calculate);
        panel.add(new JLabel(""));

        // Results
        panel.add(new JLabel("Daily Consumption (L):"));
        daily = new JTextField();
        daily.setEditable(false);
        panel.add(daily);

        panel.add(new JLabel("Average (L/day):"));
        average = new JTextField();
        average.setEditable(false);
        panel.add(average);

        panel.add(new JLabel("Minimum (L/day):"));
        minimum = new JTextField();
        minimum.setEditable(false);
        panel.add(minimum);

        panel.add(new JLabel("Maximum (L/day):"));
        maximum = new JTextField();
        maximum.setEditable(false);
        panel.add(maximum);

        panel.add(new JLabel("Variation (L):"));
        variation = new JTextField();
        variation.setEditable(false);
        panel.add(variation);

        add(panel);

        // Calculation
        calculate.addActionListener(e -> calculateValues());
    }

    private void calculateValues() {

        try {
            double d1 = Double.parseDouble(day1.getText());
            double d2 = Double.parseDouble(day2.getText());
            double d3 = Double.parseDouble(day3.getText());
            double d4 = Double.parseDouble(day4.getText());
            double d5 = Double.parseDouble(day5.getText());

            double total = d1 + d2 + d3 + d4 + d5;

            double avg = total / 5;

            double min = Math.min(
                    Math.min(d1, d2),
                    Math.min(d3, Math.min(d4, d5))
            );

            double max = Math.max(
                    Math.max(d1, d2),
                    Math.max(d3, Math.max(d4, d5))
            );

            double var = max - min;

            // Latest day's consumption
            daily.setText(String.format("%.2f", d5));

            average.setText(String.format("%.2f", avg));
            minimum.setText(String.format("%.2f", min));
            maximum.setText(String.format("%.2f", max));
            variation.setText(String.format("%.2f", var));

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ConsumptionCalculatorFrame frame =
                    new ConsumptionCalculatorFrame();

            frame.setVisible(true);
        });
    }
}