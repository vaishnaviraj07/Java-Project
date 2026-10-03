import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final List<DailyRecord> records = new ArrayList<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Date", "Consumption (L)", "Wastage (L)", "Daily Status"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JLabel dailyValue = metricValue();
    private final JLabel weeklyValue = metricValue();
    private final JLabel averageValue = metricValue();
    private final JLabel wastageValue = metricValue();
    private final JLabel abnormalValue = metricValue();
    private final JPanel alertPanel = new JPanel(new BorderLayout(8, 6));
    private final JLabel alertTitle = new JLabel();
    private final JTextArea alertText = new JTextArea();
    private final JSpinner dateInput = new JSpinner(new SpinnerDateModel());
    private final JTextField consumptionInput = new JTextField(8);
    private final JTextField wastageInput = new JTextField(8);

    public Main() {
        super("Smart Water Tank Consumption Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 660));
        setLocationRelativeTo(null);
        buildInterface();
        refreshDashboard();
    }

    private static JLabel metricValue() {
        JLabel label = new JLabel("—");
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        label.setForeground(new Color(26, 82, 118));
        return label;
    }

    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        root.setBackground(new Color(244, 248, 250));
        setContentPane(root);

        JLabel heading = new JLabel("Smart Water Tank Consumption Monitor");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        JLabel subtitle = new JLabel("Daily water use, wastage, and unusual consumption");
        subtitle.setForeground(new Color(85, 101, 112));
        JPanel headingPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        headingPanel.setOpaque(false);
        headingPanel.add(heading);
        headingPanel.add(subtitle);
        root.add(headingPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        JPanel metrics = new JPanel(new GridLayout(1, 5, 10, 10));
        metrics.setOpaque(false);
        metrics.add(metricCard("Daily consumption", dailyValue));
        metrics.add(metricCard("Weekly consumption", weeklyValue));
        metrics.add(metricCard("Average / day", averageValue));
        metrics.add(metricCard("Wastage", wastageValue));
        metrics.add(metricCard("Abnormal events", abnormalValue));
        center.add(metrics, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        table.setRowHeight(27);
        table.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Daily Records — Last 7 Days"));
        center.add(tableScroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);
        bottom.add(buildEntryPanel(), BorderLayout.NORTH);
        buildAlertPanel();
        bottom.add(alertPanel, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel metricCard(String name, JLabel value) {
        JPanel card = new JPanel(new BorderLayout(4, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 232)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JLabel caption = new JLabel(name);
        caption.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        caption.setForeground(new Color(85, 101, 112));
        card.add(caption, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildEntryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Record Tank Consumption"));
        dateInput.setEditor(new JSpinner.DateEditor(dateInput, "dd MMM yyyy"));
        panel.add(new JLabel("Date:"));
        panel.add(dateInput);
        panel.add(new JLabel("Consumption (L):"));
        panel.add(consumptionInput);
        panel.add(new JLabel("Wastage (L):"));
        panel.add(wastageInput);
        JButton saveButton = new JButton("Save Daily Record");
        saveButton.addActionListener(event -> saveRecord());
        panel.add(saveButton);
        return panel;
    }

    private void buildAlertPanel() {
        alertPanel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        alertPanel.setBackground(new Color(232, 245, 233));
        alertTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        alertText.setEditable(false);
        alertText.setOpaque(false);
        alertText.setLineWrap(true);
        alertText.setWrapStyleWord(true);
        alertText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        alertPanel.add(alertTitle, BorderLayout.NORTH);
        alertPanel.add(alertText, BorderLayout.CENTER);
    }

    private void saveRecord() {
        try {
            double consumption = parseNonNegative(consumptionInput.getText(), "consumption");
            double wastage = parseNonNegative(wastageInput.getText(), "wastage");
            LocalDate date = ((java.util.Date) dateInput.getValue()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            records.removeIf(record -> record.date.equals(date));
            records.add(new DailyRecord(date, consumption, wastage));
            records.sort(Comparator.comparing(record -> record.date));
            consumptionInput.setText("");
            wastageInput.setText("");
            refreshDashboard();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Reading", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseNonNegative(String text, String name) {
        try {
            double value = Double.parseDouble(text.trim());
            if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a valid non-negative " + name + " value in litres.");
        }
    }

    private void refreshDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        List<DailyRecord> week = records.stream()
                .filter(record -> !record.date.isBefore(weekStart) && !record.date.isAfter(today))
                .sorted(Comparator.comparing(record -> record.date))
                .toList();

        double dailyConsumption = records.stream().filter(record -> record.date.equals(today))
                .mapToDouble(record -> record.consumption).sum();
        double weeklyConsumption = week.stream().mapToDouble(record -> record.consumption).sum();
        double weeklyWastage = week.stream().mapToDouble(record -> record.wastage).sum();
        double average = week.isEmpty() ? 0 : weeklyConsumption / week.size();
        int abnormalEvents = 0;
        List<Double> earlierConsumptions = new ArrayList<>();
        List<Double> earlierWastage = new ArrayList<>();
        Map<LocalDate, String> dailyStatuses = new HashMap<>();
        for (DailyRecord record : week) {
            double earlierConsumptionAverage = earlierConsumptions.stream()
                    .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double earlierWastageAverage = earlierWastage.stream()
                    .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            boolean highConsumption = !Double.isNaN(earlierConsumptionAverage)
                    && record.consumption > earlierConsumptionAverage * 1.5;
            boolean highWastage = !Double.isNaN(earlierWastageAverage)
                    && record.wastage > earlierWastageAverage * 1.5;
            boolean wastageExceedsConsumption = record.wastage > record.consumption;
            String dailyStatus;
            if (highConsumption && (highWastage || wastageExceedsConsumption))
                dailyStatus = "ABNORMAL — BOTH";
            else if (highWastage || wastageExceedsConsumption)
                dailyStatus = "ABNORMAL — WASTAGE";
            else if (highConsumption)
                dailyStatus = "ABNORMAL — USAGE";
            else if (earlierConsumptions.isEmpty())
                dailyStatus = "BASELINE";
            else
                dailyStatus = "NORMAL";
            dailyStatuses.put(record.date, dailyStatus);
            if (dailyStatus.startsWith("ABNORMAL")) abnormalEvents++;
            earlierConsumptions.add(record.consumption);
            earlierWastage.add(record.wastage);
        }

        if (week.isEmpty()) {
            dailyValue.setText("—");
            weeklyValue.setText("—");
            averageValue.setText("—");
            wastageValue.setText("—");
            abnormalValue.setText("—");
        } else {
            dailyValue.setText(records.stream().anyMatch(record -> record.date.equals(today))
                    ? format(dailyConsumption) + " L" : "—");
            weeklyValue.setText(format(weeklyConsumption) + " L");
            averageValue.setText(format(average) + " L");
            wastageValue.setText(format(weeklyWastage) + " L");
            abnormalValue.setText(week.size() > 1 ? Integer.toString(abnormalEvents) : "—");
        }

        tableModel.setRowCount(0);
        for (int i = week.size() - 1; i >= 0; i--) {
            DailyRecord record = week.get(i);
            tableModel.addRow(new Object[]{record.date.format(DATE_FORMAT),
                    format(record.consumption), format(record.wastage), dailyStatuses.get(record.date)});
        }

        DailyRecord latest = records.stream().filter(record -> record.date.equals(today))
                .findFirst().orElse(null);
        double earlierAverage = week.stream()
                .filter(record -> record.date.isBefore(today))
                .mapToDouble(record -> record.consumption)
                .average()
                .orElse(Double.NaN);
        boolean abnormalToday = latest != null
                && dailyStatuses.getOrDefault(today, "").startsWith("ABNORMAL");
        if (abnormalToday || abnormalEvents > 0) {
            alertPanel.setBackground(new Color(255, 235, 238));
            alertTitle.setText("⚠ ABNORMAL CONSUMPTION DETECTED");
            alertTitle.setForeground(new Color(183, 28, 28));
            String timing = abnormalToday
                    ? "Today's consumption or wastage is unusually high.\n\n"
                    : "An unusually high consumption or wastage reading was found in the last 7 days.\n"
                    + "Today's reading is not above its comparison baseline.\n\n";
            alertText.setText(timing + "Possible leakage, spillage,\nor unusually high usage.\n\n"
                    + "Please inspect the system.\n"
                    + "This alert indicates a possible issue and does not physically confirm a leak.");
        } else if (week.isEmpty()) {
            alertPanel.setBackground(new Color(238, 242, 245));
            alertTitle.setText("ENTER READINGS TO VIEW STATUS");
            alertTitle.setForeground(new Color(70, 85, 95));
            alertText.setText("No consumption records have been entered yet.");
        } else if (latest == null) {
            alertPanel.setBackground(new Color(238, 242, 245));
            alertTitle.setText("WAITING FOR TODAY'S READING");
            alertTitle.setForeground(new Color(70, 85, 95));
            alertText.setText("Enter today's consumption to check it against earlier recorded days.");
        } else if (Double.isNaN(earlierAverage)) {
            alertPanel.setBackground(new Color(255, 248, 225));
            alertTitle.setText("NOT ENOUGH DATA FOR BASELINE");
            alertTitle.setForeground(new Color(143, 95, 0));
            alertText.setText("Enter consumption for at least one earlier day.\n"
                    + "Today's reading cannot be compared with a baseline yet.");
        } else {
            alertPanel.setBackground(new Color(232, 245, 233));
            alertTitle.setText("SYSTEM STATUS: NORMAL");
            alertTitle.setForeground(new Color(46, 125, 50));
            alertText.setText("No abnormal consumption detected today.\n"
                    + "Alerts indicate possible issues and do not physically confirm a leak.");
        }
    }

    private static String format(double value) {
        return String.format("%.1f", value);
    }

    private static final class DailyRecord {
        private final LocalDate date;
        private final double consumption;
        private final double wastage;

        private DailyRecord(LocalDate date, double consumption, double wastage) {
            this.date = date;
            this.consumption = consumption;
            this.wastage = wastage;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
