import javax.swing.*;
import java.awt.*;

public class LiquidRegistration extends JFrame {

    JLabel titleLabel;
    JLabel nameLabel;
    JLabel minLabel;
    JLabel maxLabel;
    JLabel thresholdLabel;

    JTextField nameField;
    JTextField minField;
    JTextField maxField;
    JTextField thresholdField;

    JButton addButton;

    public LiquidRegistration() {
        setTitle("Liquid Registration");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Title
        titleLabel = new JLabel("Liquid Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(140, 30, 250, 40);
        panel.add(titleLabel);

        // Liquid Name / Type
        nameLabel = new JLabel("Liquid Name / Type:");
        nameLabel.setBounds(60, 100, 150, 30);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(220, 100, 200, 30);
        panel.add(nameField);

        // Normal Minimum
        minLabel = new JLabel("Normal Minimum:");
        minLabel.setBounds(60, 150, 150, 30);
        panel.add(minLabel);

        minField = new JTextField();
        minField.setBounds(220, 150, 200, 30);
        panel.add(minField);

        // Normal Maximum
        maxLabel = new JLabel("Normal Maximum:");
        maxLabel.setBounds(60, 200, 150, 30);
        panel.add(maxLabel);

        maxField = new JTextField();
        maxField.setBounds(220, 200, 200, 30);
        panel.add(maxField);

        // Threshold
        thresholdLabel = new JLabel("Threshold:");
        thresholdLabel.setBounds(60, 250, 150, 30);
        panel.add(thresholdLabel);

        thresholdField = new JTextField();
        thresholdField.setBounds(220, 250, 200, 30);
        panel.add(thresholdField);

        // Add Button
        addButton = new JButton("Add Liquid");
        addButton.setBounds(180, 320, 140, 40);
        panel.add(addButton);

        // Button action
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String min = minField.getText();
            String max = maxField.getText();
            String threshold = thresholdField.getText();

            if (name.isEmpty() || min.isEmpty() ||
                max.isEmpty() || threshold.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields."
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Liquid added successfully!"
                );
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LiquidRegistration().setVisible(true);
        });
    }
}