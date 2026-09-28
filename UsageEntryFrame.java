import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UsageEntryFrame extends JFrame {

    JTextField dateField;import javax.swing.*;
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
    JTextField timeField;
    JTextField initialField;
    JTextField consumedField;
    JTextField remainingField;
    JTextField leakageAmountField;

    JComboBox<String> liquidBox;
    JComboBox<String> leakageDetectionBox;
    JComboBox<String> leakageStatusBox;

    JButton saveButton;

    public UsageEntryFrame() {

        setTitle("Smart Liquid Consumption and Leak Detection System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ================= BACKGROUND =================

        JPanel background = new JPanel() {

            int y = 0;

            {
                Timer timer = new Timer(40, e -> {
                    y++;

                    if (y > getHeight()) {
                        y = 0;
                    }

                    repaint();
                });

                timer.start();
            }

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

                // Animated liquid particles
                g2.setColor(new Color(0, 210, 255, 70));

                for (int i = 0; i < 9; i++) {

                    int waveY = y + i * 85;

                    for (int x = 0; x < getWidth(); x += 20) {

                        int yy = waveY
                                + (int) (Math.sin(x * 0.03) * 15);

                        g2.fillOval(x, yy, 4, 4);
                    }
                }

                // Background grid
                g2.setColor(new Color(0, 200, 255, 20));

                for (int x = 0; x < getWidth(); x += 50) {
                    g2.drawLine(x, 0, x, getHeight());
                }

                for (int yy = 0; yy < getHeight(); yy += 50) {
                    g2.drawLine(0, yy, getWidth(), yy);
                }
            }
        };

        background.setLayout(null);
        setContentPane(background);

        // ================= MAIN PANEL =================

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(8, 24, 40, 245));
        panel.setBounds(70, 25, 760, 610);

        background.add(panel);

        // ================= TITLE =================

        JLabel title = new JLabel("LIQUID USAGE ENTRY");

        title.setBounds(0, 20, 760, 40);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(0, 225, 255));

        panel.add(title);

        JLabel subtitle = new JLabel(
                "Smart Liquid Consumption & Leak Detection System"
        );

        subtitle.setBounds(0, 58, 760, 25);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.LIGHT_GRAY);

        panel.add(subtitle);

        // ================= LIQUID TYPE =================

        JLabel liquidLabel = createLabel("Liquid Type");
        liquidLabel.setBounds(60, 100, 220, 30);
        panel.add(liquidLabel);

        liquidBox = new JComboBox<>();

        liquidBox.addItem("Water");
        liquidBox.addItem("Petrol");
        liquidBox.addItem("Diesel");
        liquidBox.addItem("Oil");
        liquidBox.addItem("Chemical");
        liquidBox.addItem("Other");

        liquidBox.setBounds(300, 95, 390, 40);
        liquidBox.setFont(new Font("Arial", Font.PLAIN, 14));
        liquidBox.setBackground(new Color(20, 40, 55));
        liquidBox.setForeground(Color.WHITE);

        panel.add(liquidBox);

        // ================= DATE =================

        JLabel dateLabel = createLabel("Date");
        dateLabel.setBounds(60, 150, 220, 30);
        panel.add(dateLabel);

        dateField = createField("DD/MM/YYYY");
        dateField.setBounds(300, 145, 390, 40);

        panel.add(dateField);

        // ================= TIME =================

        JLabel timeLabel = createLabel("Time");
        timeLabel.setBounds(60, 200, 220, 30);
        panel.add(timeLabel);

        timeField = createField("HH:MM");
        timeField.setBounds(300, 195, 390, 40);

        panel.add(timeField);

        // ================= INITIAL QUANTITY =================

        JLabel initialLabel =
                createLabel("Initial Quantity (Litres)");

        initialLabel.setBounds(60, 250, 220, 30);
        panel.add(initialLabel);

        initialField =
                createField("Enter initial quantity");

        initialField.setBounds(300, 245, 390, 40);

        panel.add(initialField);

        // ================= CONSUMED QUANTITY =================

        JLabel consumedLabel =
                createLabel("Consumed Quantity (Litres)");

        consumedLabel.setBounds(60, 300, 220, 30);
        panel.add(consumedLabel);

        consumedField =
                createField("Enter consumed quantity");

        consumedField.setBounds(300, 295, 390, 40);

        panel.add(consumedField);

        // ================= REMAINING QUANTITY =================

        JLabel remainingLabel =
                createLabel("Remaining Quantity (Litres)");

        remainingLabel.setBounds(60, 350, 220, 30);
        panel.add(remainingLabel);

        remainingField =
                createField("Automatically calculated");

        remainingField.setBounds(300, 345, 390, 40);
        remainingField.setEditable(false);
        remainingField.setBackground(
                new Color(25, 50, 65)
        );

        panel.add(remainingField);

        // ================= LEAKAGE DETECTION =================

        JLabel leakageDetectionLabel =
                createLabel("Leakage Detection");

        leakageDetectionLabel.setBounds(60, 400, 220, 30);
        panel.add(leakageDetectionLabel);

        leakageDetectionBox = new JComboBox<>();

        leakageDetectionBox.addItem("Select");
        leakageDetectionBox.addItem("Yes");
        leakageDetectionBox.addItem("No");

        leakageDetectionBox.setBounds(300, 395, 390, 40);
        leakageDetectionBox.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );
        leakageDetectionBox.setBackground(
                new Color(20, 40, 55)
        );
        leakageDetectionBox.setForeground(Color.WHITE);

        panel.add(leakageDetectionBox);

        // ================= LEAKAGE AMOUNT =================

        JLabel leakageAmountLabel =
                createLabel("Leakage Amount (Litres)");

        leakageAmountLabel.setBounds(60, 450, 220, 30);
        panel.add(leakageAmountLabel);

        leakageAmountField =
                createField("Enter leakage amount");

        leakageAmountField.setBounds(300, 445, 390, 40);

        panel.add(leakageAmountField);

        // ================= LEAKAGE STATUS =================

        JLabel leakageStatusLabel =
                createLabel("Leakage Status");

        leakageStatusLabel.setBounds(60, 500, 220, 30);
        panel.add(leakageStatusLabel);

        leakageStatusBox = new JComboBox<>();

        leakageStatusBox.addItem("Normal");
        leakageStatusBox.addItem("Suspected Leak");
        leakageStatusBox.addItem("Leak Detected");

        leakageStatusBox.setBounds(300, 495, 390, 40);
        leakageStatusBox.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );
        leakageStatusBox.setBackground(
                new Color(20, 40, 55)
        );
        leakageStatusBox.setForeground(Color.WHITE);

        panel.add(leakageStatusBox);

        // ================= SAVE BUTTON =================

        saveButton =
                new JButton("SAVE USAGE ENTRY");

        saveButton.setBounds(270, 550, 220, 42);

        saveButton.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(
                new Color(0, 145, 200)
        );

        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        panel.add(saveButton);

        // ================= BUTTON HOVER =================

        saveButton.addMouseListener(
                new MouseAdapter() {

                    public void mouseEntered(MouseEvent e) {

                        saveButton.setBackground(
                                new Color(0, 210, 240)
                        );
                    }

                    public void mouseExited(MouseEvent e) {

                        saveButton.setBackground(
                                new Color(0, 145, 200)
                        );
                    }
                }
        );

        // ================= SAVE ACTION =================

        saveButton.addActionListener(
                e -> saveData()
        );
    }

    // =====================================================
    // CREATE LABEL
    // =====================================================

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(Color.WHITE);

        return label;
    }

    // =====================================================
    // CREATE ROUNDED INPUT FIELD
    // =====================================================

    private JTextField createField(String text) {

        JTextField field = new JTextField();

        field.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        field.setForeground(Color.WHITE);

        field.setCaretColor(
                new Color(0, 220, 255)
        );

        field.setBackground(
                new Color(20, 40, 55)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(0, 180, 220),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 15, 5, 15
                        )
                )
        );

        field.setToolTipText(text);

        // Mouse hover animation
        field.addMouseListener(
                new MouseAdapter() {

                    public void mouseEntered(MouseEvent e) {

                        field.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(0, 240, 255),
                                                2
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                5, 15, 5, 15
                                        )
                                )
                        );
                    }

                    public void mouseExited(MouseEvent e) {

                        field.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(0, 180, 220),
                                                1
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                5, 15, 5, 15
                                        )
                                )
                        );
                    }
                }
        );

        return field;
    }

    // =====================================================
    // SAVE DATA
    // =====================================================

    private void saveData() {

        String liquid =
                liquidBox.getSelectedItem().toString();

        String date =
                dateField.getText().trim();

        String time =
                timeField.getText().trim();

        String initialText =
                initialField.getText().trim();

        String consumedText =
                consumedField.getText().trim();

        String leakageDetection =
                leakageDetectionBox
                        .getSelectedItem()
                        .toString();

        String leakageAmount =
                leakageAmountField
                        .getText()
                        .trim();

        String leakageStatus =
                leakageStatusBox
                        .getSelectedItem()
                        .toString();

        // Check required fields
        if (date.isEmpty()
                || time.isEmpty()
                || initialText.isEmpty()
                || consumedText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            double initial =
                    Double.parseDouble(initialText);

            double consumed =
                    Double.parseDouble(consumedText);

            if (initial < 0 || consumed < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity cannot be negative.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (consumed > initial) {

                JOptionPane.showMessageDialog(
                        this,
                        "Consumed quantity cannot be greater than initial quantity.",
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            double remaining =
                    initial - consumed;

            remainingField.setText(
                    String.format("%.2f", remaining)
            );

            // If leakage is detected but amount is empty
            if (leakageDetection.equals("Yes")
                    && leakageAmount.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the leakage amount.",
                        "Leakage Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Usage Entry Saved Successfully!\n\n"
                    + "Liquid Type: " + liquid + "\n"
                    + "Date: " + date + "\n"
                    + "Time: " + time + "\n"
                    + "Initial Quantity: "
                    + initial + " L\n"
                    + "Consumed Quantity: "
                    + consumed + " L\n"
                    + "Remaining Quantity: "
                    + String.format("%.2f", remaining)
                    + " L\n"
                    + "Leakage Detection: "
                    + leakageDetection + "\n"
                    + "Leakage Amount: "
                    + (leakageAmount.isEmpty()
                    ? "0"
                    : leakageAmount)
                    + " L\n"
                    + "Leakage Status: "
                    + leakageStatus,
                    "Entry Saved"
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers for quantity.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    UsageEntryFrame frame =
                            new UsageEntryFrame();

                    frame.setVisible(true);
                }
        );
    }
}