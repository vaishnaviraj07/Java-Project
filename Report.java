
import javax.swing.*;
import java.awt.*;

public class Report extends JFrame {

    public Report(double usage) {

        setTitle("Consumption Report");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(
                new BorderLayout(12, 18)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        panel.setBackground(new Color(245, 249, 253));

        JLabel title = new JLabel(
                "Consumption Report",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(30, 65, 95));

        panel.add(title, BorderLayout.NORTH);

        JPanel rows = new JPanel(
                new GridLayout(5, 1, 0, 10)
        );

        rows.setOpaque(false);

        String[] names = {
                "Daily Consumption",
                "Weekly Estimate",
                "Today's Average",
                "Estimated Wastage",
                "Abnormal Events"
        };

        String[] values = {
                String.format("%.2f L", usage),
                String.format("%.2f L", usage * 7),
                String.format("%.2f L/day", usage),
                "Not measured",
                usage > 25 ? "1" : "0"
        };

        for (int i = 0; i < names.length; i++) {

            JLabel row = new JLabel(
                    "  " + names[i] + "     :     " + values[i]
            );

            row.setFont(new Font("Arial", Font.PLAIN, 16));
            row.setOpaque(true);
            row.setBackground(Color.WHITE);

            row.setBorder(
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            );

            rows.add(row);
        }

        panel.add(rows, BorderLayout.CENTER);

        add(panel);
    }

    // Main method to run Report independently
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Report report = new Report(40.0);
            report.setVisible(true);
        });
    }
}
