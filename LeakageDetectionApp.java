import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LeakageDetectionApp extends JFrame {
    private static final Color NAVY = new Color(20, 43, 67);
    private static final Color BLUE = new Color(28, 119, 190);
    private static final Color PALE_BLUE = new Color(235, 246, 253);
    private static final Color MUTED = new Color(103, 120, 137);
    private static final Color GREEN = new Color(31, 139, 94);
    private static final Color RED = new Color(190, 62, 62);
    private static final DecimalFormat NUMBER = new DecimalFormat("#,##0.##");

    private final ArrayList<Double> readings = new ArrayList<>();
    private final JTextField consumptionInput = new JTextField();
    private final JTextField wastageInput = new JTextField();
    private final JLabel totalValue = metricValue("0 L");
    private final JLabel averageValue = metricValue("0 L");
    private final JLabel statusValue = metricValue("Awaiting readings");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"TIME", "CONSUMPTION", "WASTAGE", "STATUS"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public LeakageDetectionApp() {
        setTitle("Leakage Detection | Water Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 620));
        getContentPane().setBackground(new Color(245, 248, 251));
        setLayout(new BorderLayout());

        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(28, 34, 28, 34));
        page.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);
        content.add(createTopContent(), BorderLayout.NORTH);
        content.add(createHistory(), BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);

        pack();
        setSize(900, 680);
        setLocationRelativeTo(null);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        JLabel eyebrow = new JLabel("WATER USAGE OVERVIEW");
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 11));
        eyebrow.setForeground(BLUE);
        JLabel title = new JLabel("Leakage Detection");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Track consumption and catch unusual usage early.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        titles.add(eyebrow);
        titles.add(Box.createVerticalStrut(4));
        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);

        JLabel live = new JLabel("●  MONITOR ACTIVE");
        live.setOpaque(true);
        live.setBackground(new Color(230, 246, 238));
        live.setForeground(GREEN);
        live.setFont(new Font("SansSerif", Font.BOLD, 11));
        live.setBorder(new EmptyBorder(9, 12, 9, 12));
        header.add(titles, BorderLayout.WEST);
        header.add(live, BorderLayout.EAST);
        return header;
    }

    private JPanel createTopContent() {
        JPanel top = new JPanel(new BorderLayout(0, 18));
        top.setOpaque(false);
        JPanel metrics = new JPanel(new GridLayout(1, 3, 14, 0));
        metrics.setOpaque(false);
        metrics.add(metricCard("TOTAL CONSUMPTION", totalValue, "Across all saved readings"));
        metrics.add(metricCard("AVERAGE PER READING", averageValue, "Your usage baseline"));
        metrics.add(metricCard("LATEST STATUS", statusValue, "Based on current reading"));
        top.add(metrics, BorderLayout.NORTH);
        top.add(createEntryCard(), BorderLayout.CENTER);
        return top;
    }

    private JPanel createEntryCard() {
        JPanel card = cardPanel();
        card.setLayout(new BorderLayout(16, 12));
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Add a reading");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(NAVY);
        JLabel hint = new JLabel("Enter the latest meter values in litres.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 13));
        hint.setForeground(MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(hint);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        addInput(fields, c, 0, "Consumption (L)", consumptionInput);
        addInput(fields, c, 1, "Wastage (L)", wastageInput);
        JButton saveButton = new JButton("Save reading");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(BLUE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(event -> saveReading());
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0;
        c.insets = new Insets(20, 0, 0, 0);
        fields.add(saveButton, c);

        card.add(heading, BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);
        return card;
    }

    private void addInput(JPanel panel, GridBagConstraints c, int column, String label, JTextField field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 7));
        wrapper.setOpaque(false);
        JLabel caption = new JLabel(label);
        caption.setFont(new Font("SansSerif", Font.BOLD, 12));
        caption.setForeground(NAVY);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(NAVY);
        field.setBackground(new Color(249, 251, 253));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 227, 235)),
                new EmptyBorder(10, 11, 10, 11)));
        wrapper.add(caption, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        c.gridx = column;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 12);
        c.weightx = 1;
        panel.add(wrapper, c);
    }

    private JPanel createHistory() {
        JPanel card = cardPanel();
        card.setLayout(new BorderLayout(0, 14));
        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        JLabel title = new JLabel("Reading history");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(NAVY);
        JLabel count = new JLabel("NEWEST FIRST");
        count.setFont(new Font("SansSerif", Font.BOLD, 10));
        count.setForeground(MUTED);
        heading.add(title, BorderLayout.WEST);
        heading.add(count, BorderLayout.EAST);

        JTable table = new JTable(tableModel);
        table.setRowHeight(38);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(NAVY);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setBackground(new Color(247, 250, 252));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 235, 240)));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                    boolean focus, int row, int column) {
                Component component = super.getTableCellRendererComponent(t, value, selected, focus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!selected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 252, 253));
                    setForeground(column == 3 && "REVIEW".equals(value) ? RED : NAVY);
                    if (column == 3) setFont(getFont().deriveFont(Font.BOLD));
                }
                return component;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229, 235, 240)));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(heading, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel metricCard(String label, JLabel value, String detail) {
        JPanel card = cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel caption = new JLabel(label);
        caption.setFont(new Font("SansSerif", Font.BOLD, 10));
        caption.setForeground(MUTED);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel foot = new JLabel(detail);
        foot.setFont(new Font("SansSerif", Font.PLAIN, 11));
        foot.setForeground(MUTED);
        card.add(caption);
        card.add(Box.createVerticalStrut(9));
        card.add(value);
        card.add(Box.createVerticalStrut(5));
        card.add(foot);
        return card;
    }

    private static JLabel metricValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 19));
        label.setForeground(NAVY);
        return label;
    }

    private JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 236, 241)),
                new EmptyBorder(17, 18, 17, 18)));
        return panel;
    }

    private void saveReading() {
        try {
            double consumption = Double.parseDouble(consumptionInput.getText().trim());
            double wastage = Double.parseDouble(wastageInput.getText().trim());
            if (!Double.isFinite(consumption) || !Double.isFinite(wastage)
                    || consumption < 0 || wastage < 0) throw new NumberFormatException();

            double average = readings.stream().mapToDouble(Double::doubleValue).average().orElse(-1);
            readings.add(consumption);
            boolean abnormal = wastage > consumption || (average >= 0 && consumption > average * 1.5);
            String status = abnormal ? "REVIEW" : "NORMAL";
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            tableModel.insertRow(0, new Object[]{time, NUMBER.format(consumption) + " L",
                    NUMBER.format(wastage) + " L", status});

            double total = readings.stream().mapToDouble(Double::doubleValue).sum();
            double currentAverage = total / readings.size();
            totalValue.setText(NUMBER.format(total) + " L");
            averageValue.setText(NUMBER.format(currentAverage) + " L");
            statusValue.setText(abnormal ? "Review reading" : "Normal");
            statusValue.setForeground(abnormal ? RED : GREEN);
            consumptionInput.setText("");
            wastageInput.setText("");
            consumptionInput.requestFocusInWindow();
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(this,
                    "Enter valid numbers greater than or equal to zero.",
                    "Invalid reading", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeakageDetectionApp().setVisible(true));
    }
}
