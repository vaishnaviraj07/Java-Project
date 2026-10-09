
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UsageEntryFrame extends JFrame {

    private JTextField dateField;
    private JTextField initialField;
    private JTextField consumedField;
    private JTextField remainingField;
    private JTextField leakageAmountField;

    private JComboBox<String> liquidBox;
    private JComboBox<String> leakageDetectionBox;
    private JComboBox<String> leakageStatusBox;

    public UsageEntryFrame() {

        setTitle("Usage Entry");
        setSize(520, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        // Liquid Type
        panel.add(new JLabel("Liquid Type:"));

        liquidBox = new JComboBox<>(new String[]{
                "Water", "Fuel", "Oil", "Chemical", "Other"
        });
        panel.add(liquidBox);

        // Date
        panel.add(new JLabel("Date (yyyy-MM-dd):"));

        dateField = new JTextField(LocalDate.now().toString());
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
                new String[]{"No", "Yes"}
        );
        panel.add(leakageDetectionBox);

        // Leakage Amount
        panel.add(new JLabel("Leakage Amount (L):"));

        leakageAmountField = new JTextField("0");
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

        // Update remaining quantity when values change
        initialField.addActionListener(e -> calculateRemaining());
        consumedField.addActionListener(e -> calculateRemaining());
        leakageAmountField.addActionListener(e -> calculateRemaining());

        // Keep leakage information consistent
        leakageDetectionBox.addActionListener(e -> {
            if ("No".equals(leakageDetectionBox.getSelectedItem())) {
                leakageAmountField.setText("0");
                leakageStatusBox.setSelectedItem("Normal");
            } else if ("Normal".equals(
                    leakageStatusBox.getSelectedItem())) {
                leakageStatusBox.setSelectedItem("Suspected Leak");
            }
            calculateRemaining();
        });

        leakageStatusBox.addActionListener(e -> {
            if ("Suspected Leak".equals(
                    leakageStatusBox.getSelectedItem())
                    || "Leak Detected".equals(
                    leakageStatusBox.getSelectedItem())) {
                leakageDetectionBox.setSelectedItem("Yes");
            }
        });

        saveButton.addActionListener(e -> saveData());
    }

    // Parse and validate a non-negative quantity
    private double readQuantity(JTextField field, String name)
            throws IllegalArgumentException {

        String value = field.getText().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    name + " is required."
            );
        }

        try {
            double quantity = Double.parseDouble(value);

            if (!Double.isFinite(quantity) || quantity < 0) {
                throw new IllegalArgumentException(
                        name + " must be a non-negative number."
                );
            }

            return quantity;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    name + " must be a valid number."
            );
        }
    }

    // Calculate remaining quantity after consumption and leakage
    private boolean calculateRemaining() {

        try {
            double initial = readQuantity(
                    initialField, "Initial quantity"
            );

            double consumed = readQuantity(
                    consumedField, "Consumed quantity"
            );

            double leakage = readQuantity(
                    leakageAmountField, "Leakage amount"
            );

            if (consumed > initial) {
                remainingField.setText("");
                return false;
            }

            if (leakage > initial - consumed) {
                remainingField.setText("");
                return false;
            }

            double remaining = initial - consumed - leakage;

            remainingField.setText(
                    String.format("%.2f", remaining)
            );

            return true;

        } catch (IllegalArgumentException ex) {
            remainingField.setText("");
            return false;
        }
    }

    // Validate and save the entry
    private void saveData() {

        try {
            // Validate date
            LocalDate.parse(dateField.getText().trim());

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid date in yyyy-MM-dd format.",
                    "Date Error",
                    JOptionPane.WARNING_MESSAGE
            );
            dateField.requestFocus();
            return;
        }

        double initial;
        double consumed;
        double leakage;

        try {
            initial = readQuantity(
                    initialField, "Initial quantity"
            );

            consumed = readQuantity(
                    consumedField, "Consumed quantity"
            );

            leakage = readQuantity(
                    leakageAmountField, "Leakage amount"
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (consumed > initial) {
            JOptionPane.showMessageDialog(
                    this,
                    "Consumed quantity cannot exceed initial quantity.",
                    "Quantity Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (leakage > initial - consumed) {
            JOptionPane.showMessageDialog(
                    this,
                    "Leakage amount cannot exceed the available quantity.",
                    "Leakage Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String detection =
                (String) leakageDetectionBox.getSelectedItem();

        String status =
                (String) leakageStatusBox.getSelectedItem();

        if ("No".equals(detection)) {
            if (leakage > 0 || !"Normal".equals(status)) {
                JOptionPane.showMessageDialog(
                        this,
                        "When leakage detection is No, "
                                + "leakage must be 0 and status must be Normal.",
                        "Leakage Validation",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } else if (leakage > 0 && "Normal".equals(status)) {
            JOptionPane.showMessageDialog(
                    this,
                    "A positive leakage amount cannot have Normal status.",
                    "Leakage Validation",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Update remaining quantity only after validation
        calculateRemaining();

        // Display saved entry details
        String message =
                "Usage entry validated successfully!\n\n"
                + "Liquid: " + liquidBox.getSelectedItem() + "\n"
                + "Date: " + dateField.getText().trim() + "\n"
                + "Initial Quantity: " + initial + " L\n"
                + "Consumed Quantity: " + consumed + " L\n"
                + "Leakage Amount: " + leakage + " L\n"
                + "Remaining Quantity: "
                + remainingField.getText() + " L\n"
                + "Leakage Detection: " + detection + "\n"
                + "Leakage Status: " + status;

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            UsageEntryFrame frame = new UsageEntryFrame();
            frame.setVisible(true);
        });
    }
}
