import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Smart Liquid Consumption & Leak Detection");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("Smart Liquid Consumption Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        mainPanel.add(title, BorderLayout.NORTH);

        // Cards panel
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        cardsPanel.setBackground(Color.WHITE);

        cardsPanel.add(createCard("Current Water Level", "75%"));
        cardsPanel.add(createCard("Today's Consumption", "120 L"));
        cardsPanel.add(createCard("Average Consumption", "105 L"));
        cardsPanel.add(createCard("Leak Status", "No Anomaly"));

        mainPanel.add(cardsPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton historyButton = new JButton("Consumption History");
        JButton alertsButton = new JButton("Alerts");
        JButton reportsButton = new JButton("Reports");

        buttonPanel.add(historyButton);
        buttonPanel.add(alertsButton);
        buttonPanel.add(reportsButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createCard(String title, String value) {

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(2, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));

        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}