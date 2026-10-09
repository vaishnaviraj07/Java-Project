import javax.swing.*;
import java.awt.*;

public class UsageEntryFrame extends JFrame {

    JTextField dateField;
    JTextField initialField;
    JTextField consumedField;
    JTextField remainingField;
    JTextField leakageAmountField;

    JComboBox<String> liquidBox;
    JComboBox<String> leakageDetectionBox;
    JComboBox<String> leakageStatusBox;

    public UsageEntryFrame() {

        setTitle("Usage Entry");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        // Liquid Type
        panel.add(new JLabel("Liquid Type:"));

        liquidBox = new JComboBox<>(
                new String[]{
                        "Water",
                        "Fuel",
                        "Oil",
                        "Chemical",
                        "Other"
                }
        );

        panel.add(liquidBox);

        // Date
        panel.add(new JLabel("Date:"));

        dateField = new JTextField();
        panel.add(dateField);

        // Initial Quantity
        panel.add(new JLabel("Initial Quantity (L):"));

        initialField = new JTextField();
        panel.add(initialField);

        // Consumed Quantity
        panel.add(new JLabel("Consumed Quantity (L):"));

        consumedField = new JTextField();
        panel.add(consumedField);

        // Remaining Quantity
        panel.add(new JLabel("Remaining Quantity (L):"));

        remainingField = new JTextField();
        remainingField.setEditable(false);
        panel.add(remainingField);

        // Leakage Detection
        panel.add(new JLabel("Leakage Detection:"));

        leakageDetectionBox = new JComboBox<>(
                new String[]{
                        "No",
                        "Yes"
                }
        );

        panel.add(leakageDetectionBox);

        // Leakage Amount
        panel.add(new JLabel("Leakage Amount (L):"));

        leakageAmountField = new JTextField();
        panel.add(leakageAmountField);

        // Leakage Status
        panel.add(new JLabel("Leakage Status:"));

        leakageStatusBox = new JComboBox<>(
                new String[]{
                        "Normal",
                        "Suspected Leak",
                        "Leak Detected"
                }
        );

        panel.add(leakageStatusBox);

        // Save button
        JButton saveButton = new JButton("Save Usage Entry");

        panel.add(saveButton);
        panel.add(new JLabel(""));

        add(panel);

        // Calculate remaining quantity
        consumedField.addActionListener(e -> calculateRemaining());
        initialField.addActionListener(e -> calculateRemaining());

        // Save button
        saveButton.addActionListener(e -> saveData());
    }

    private void calculateRemaining() {

        try {

            double initial =
                    Double.parseDouble(initialField.getText());

            double consumed =
                    Double.parseDouble(consumedField.getText());

            double remaining = initial - consumed;

            if (remaining < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Consumed quantity cannot be greater than initial quantity."
                );

                remainingField.setText("");
                return;
            }

            remainingField.setText(
                    String.format("%.2f", remaining)
            );

        } catch (NumberFormatException ex) {

            remainingField.setText("");
        }
    }

    private void saveData() {

        if (dateField.getText().isEmpty()
                || initialField.getText().isEmpty()
                || consumedField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        calculateRemaining();

        JOptionPane.showMessageDialog(
                this,
                "Usage entry saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            UsageEntryFrame frame =
                    new UsageEntryFrame();

            frame.setVisible(true);
        });
    }
}