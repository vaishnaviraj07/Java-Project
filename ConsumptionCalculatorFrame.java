
import javax.swing.*;
import java.awt.*;

public class ConsumptionCalculatorFrame extends JFrame {

    private JTextField[] dayFields = new JTextField[5];

    private JTextField daily;
    private JTextField average;
    private JTextField minimum;
    private JTextField maximum;
    private JTextField variation;

    public ConsumptionCalculatorFrame() {

        setTitle("Consumption Calculator");
        setSize(520, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        // Daily consumption inputs
        for (int i = 0; i < 5; i++) {

            panel.add(new JLabel(
                    "Day " + (i + 1) + " Consumption (L):"
            ));

            dayFields[i] = new JTextField();
            panel.add(dayFields[i]);
        }

        // Calculate button
        JButton calculate = new JButton("Calculate");
        panel.add(calculate);
        panel.add(new JLabel(""));

        // Result fields
        panel.add(new JLabel("Latest Day Consumption (L):"));
        daily = createResultField();
        panel.add(daily);

        panel.add(new JLabel("Average (L/day):"));
        average = createResultField();
        panel.add(average);

        panel.add(new JLabel("Minimum (L/day):"));
        minimum = createResultField();
        panel.add(minimum);

        panel.add(new JLabel("Maximum (L/day):"));
        maximum = createResultField();
        panel.add(maximum);

        panel.add(new JLabel("Variation (L):"));
        variation = createResultField();
        panel.add(variation);

        add(panel);

        calculate.addActionListener(e -> calculateValues());
    }

    // Create a read-only result field
    private JTextField createResultField() {

        JTextField field = new JTextField();
        field.setEditable(false);

        return field;
    }

    // Clear previous results
    private void clearResults() {

        daily.setText("");
        average.setText("");
        minimum.setText("");
        maximum.setText("");
        variation.setText("");
    }

    // Validate input and calculate results
    private void calculateValues() {

        clearResults();

        double[] values = new double[5];

        for (int i = 0; i < dayFields.length; i++) {

            String input = dayFields[i].getText().trim();

            if (input.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter consumption for Day "
                                + (i + 1) + ".",
                        "Missing Input",
                        JOptionPane.WARNING_MESSAGE
                );

                dayFields[i].requestFocus();
                return;
            }

            try {

                values[i] = Double.parseDouble(input);

                if (!Double.isFinite(values[i])
                        || values[i] < 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Day " + (i + 1)
                                    + " must contain a finite, "
                                    + "non-negative number.",
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE
                    );

                    dayFields[i].requestFocus();
                    return;
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid number for Day "
                                + (i + 1) + ".",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );

                dayFields[i].requestFocus();
                return;
            }
        }

        // Calculate total consumption
        double total = 0;

        for (double value : values) {
            total += value;
        }

        // Prevent invalid results from arithmetic overflow
        if (!Double.isFinite(total)) {

            JOptionPane.showMessageDialog(
                    this,
                    "The total is too large to calculate safely.",
                    "Calculation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        double avg = total / values.length;
        double min = values[0];
        double max = values[0];

        for (double value : values) {

            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        double var = max - min;

        // Display results
        daily.setText(String.format("%.2f", values[4]));
        average.setText(String.format("%.2f", avg));
        minimum.setText(String.format("%.2f", min));
        maximum.setText(String.format("%.2f", max));
        variation.setText(String.format("%.2f", var));
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ConsumptionCalculatorFrame frame =
                    new ConsumptionCalculatorFrame();

            frame.setVisible(true);
        });
    }
}
