import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConsumptionCalculatorFrame extends JFrame {

    JTextField day1Field;
    JTextField day2Field;
    JTextField day3Field;
    JTextField day4Field;
    JTextField day5Field;

    JTextField dailyField;
    JTextField averageField;
    JTextField minimumField;
    JTextField maximumField;
    JTextField variationField;

    JButton calculateButton;

    public ConsumptionCalculatorFrame() {

        setTitle("Smart Liquid Consumption and Leak Detection System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background
        JPanel background = new JPanel() {

            int move = 0;

            Timer timer = new Timer(40, e -> {
                move++;
                if (move > getHeight()) {
                    move = 0;
                }
                repaint();
            });

            {
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(
                        0, 0,
                        new Color(3, 12, 28),
                        getWidth(), getHeight(),
                        new Color(0, 75, 95)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Liquid animation
                for (int i = 0; i < 10; i++) {

                    int y = move + i * 80;

                    g2.setColor(new Color(0, 210, 255, 60));

                    for (int x = 0; x < getWidth(); x += 25) {

                        int yy = y + (int) (Math.sin(x * 0.04) * 12);

                        g2.fillOval(x, yy, 5, 5);
                    }
                }

                // Grid
                g2.setColor(new Color(0, 200, 255, 20));

                for (int x = 0; x < getWidth(); x += 50) {
                    g2.drawLine(x, 0, x, getHeight());
                }

                for (int y = 0; y < getHeight(); y += 50) {
                    g2.drawLine(0, y, getWidth(), y);
                }
            }
        };

        background.setLayout(null);
        setContentPane(background);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(8, 24, 40, 245));
        panel.setBounds(70, 30, 760, 610);

        background.add(panel);

        // Title
        JLabel title = new JLabel("CONSUMPTION CALCULATOR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(190, 20, 450, 40);
        panel.add(title);

        JLabel subtitle = new JLabel("Enter daily consumption values");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(150, 220, 240));
        subtitle.setBounds(255, 60, 300, 25);
        panel.add(subtitle);

        // Input section
        JLabel inputTitle = new JLabel("DAILY CONSUMPTION");
        inputTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        inputTitle.setForeground(new Color(0, 220, 255));
        inputTitle.setBounds(45, 100, 250, 30);
        panel.add(inputTitle);

        // Day fields
        addLabel(panel, "Day 1 (L)", 45, 145);
        day1Field = addField(panel, 45, 170);

        addLabel(panel, "Day 2 (L)", 200, 145);
        day2Field = addField(panel, 200, 170);

        addLabel(panel, "Day 3 (L)", 355, 145);
        day3Field = addField(panel, 355, 170);

        addLabel(panel, "Day 4 (L)", 510, 145);
        day4Field = addField(panel, 510, 170);

        addLabel(panel, "Day 5 (L)", 665, 145);
        day5Field = addField(panel, 665, 170);

        // Result section
        JLabel resultTitle = new JLabel("CALCULATION RESULTS");
        resultTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultTitle.setForeground(new Color(0, 220, 255));
        resultTitle.setBounds(45, 235, 250, 30);
        panel.add(resultTitle);

        addLabel(panel, "Daily Consumption (L)", 45, 280);
        dailyField = addField(panel, 45, 305);
        dailyField.setEditable(false);

        addLabel(panel, "Average (L/day)", 200, 280);
        averageField = addField(panel, 200, 305);
        averageField.setEditable(false);

        addLabel(panel, "Minimum (L/day)", 355, 280);
        minimumField = addField(panel, 355, 305);
        minimumField.setEditable(false);

        addLabel(panel, "Maximum (L/day)", 510, 280);
        maximumField = addField(panel, 510, 305);
        maximumField.setEditable(false);

        addLabel(panel, "Variation (L)", 665, 280);
        variationField = addField(panel, 665, 305);
        variationField.setEditable(false);

        // Calculate button
        calculateButton = new JButton("CALCULATE");

        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setBackground(new Color(0, 140, 180));
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createEmptyBorder());
        calculateButton.setBounds(280, 390, 200, 50);

        panel.add(calculateButton);

        // Button hover animation
        calculateButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                calculateButton.setBackground(new Color(0, 190, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                calculateButton.setBackground(new Color(0, 140, 180));
            }
        });

        calculateButton.addActionListener(e -> calculate());

        // Information
        JLabel info = new JLabel(
                "<html>Average = Total Consumption / Number of Days<br>" +
                "Variation = Maximum - Minimum</html>"
        );

        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(new Color(170, 210, 220));
        info.setBounds(220, 470, 350, 60);

        panel.add(info);
    }

    // Add label
    private void addLabel(JPanel panel, String text, int x, int y) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 140, 25);

        panel.add(label);
    }

    // Add input field
    private JTextField addField(JPanel panel, int x, int y) {

        JTextField field = new JTextField();

        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(20, 45, 60));

        field.setCaretColor(Color.WHITE);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 190, 220), 1
                ),
                BorderFactory.createEmptyBorder(
                        5, 10, 5, 10
                )
        ));

        field.setBounds(x, y, 125, 38);

        panel.add(field);

        return field;
    }

    // Calculation
    private void calculate() {

        try {

            double day1 = Double.parseDouble(day1Field.getText());
            double day2 = Double.parseDouble(day2Field.getText());
            double day3 = Double.parseDouble(day3Field.getText());
            double day4 = Double.parseDouble(day4Field.getText());
            double day5 = Double.parseDouble(day5Field.getText());

            if (day1 < 0 || day2 < 0 || day3 < 0 ||
                day4 < 0 || day5 < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Consumption cannot be negative.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            double total = day1 + day2 + day3 + day4 + day5;

            double average = total / 5;

            double minimum = Math.min(
                    Math.min(day1, day2),
                    Math.min(
                            Math.min(day3, day4),
                            day5
                    )
            );

            double maximum = Math.max(
                    Math.max(day1, day2),
                    Math.max(
                            Math.max(day3, day4),
                            day5
                    )
            );

            double variation = maximum - minimum;

            // Latest day
            double dailyConsumption = day5;

            dailyField.setText(String.format("%.2f", dailyConsumption));
            averageField.setText(String.format("%.2f", average));
            minimumField.setText(String.format("%.2f", minimum));
            maximumField.setText(String.format("%.2f", maximum));
            variationField.setText(String.format("%.2f", variation));

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers in all five fields.",
                    "Invalid Input",
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