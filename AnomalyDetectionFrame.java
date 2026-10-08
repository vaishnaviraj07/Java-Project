import javax.swing.*;
import java.awt.*;

public class AnomalyDetectionFrame extends JFrame {

    public AnomalyDetectionFrame() {

        setTitle("Anomaly Detection");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Anomaly Detection");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel todayLabel =
                new JLabel("Today's Usage       : 40 L");

        JLabel averageLabel =
                new JLabel("Average Usage      : 23 L");

        JLabel rangeLabel =
                new JLabel("Expected Range    : 20 - 25 L");

        JLabel statusLabel =
                new JLabel("Status: ABNORMAL USAGE");

        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(Box.createVerticalStrut(30));
        panel.add(title);

        panel.add(Box.createVerticalStrut(40));
        panel.add(todayLabel);

        panel.add(Box.createVerticalStrut(15));
        panel.add(averageLabel);

        panel.add(Box.createVerticalStrut(15));
        panel.add(rangeLabel);

        panel.add(Box.createVerticalStrut(40));
        panel.add(statusLabel);

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new AnomalyDetectionFrame();
    }
}