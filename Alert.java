import javax.swing.*;
import java.awt.*;

public class Alert extends JFrame {

    public Alert() {
        setTitle("Consumption Alert");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 190, 190), 2),
                BorderFactory.createEmptyBorder(25, 20, 20, 20)));
        panel.setBackground(new Color(255, 245, 245));

        JLabel title = new JLabel("⚠  ABNORMAL CONSUMPTION");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(180, 45, 45));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("Possible leakage, spillage, or unusually high usage.");
        message.setFont(new Font("Arial", Font.PLAIN, 14));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel inspect = new JLabel("Please inspect the system.");
        inspect.setFont(new Font("Arial", Font.BOLD, 16));
        inspect.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel note = new JLabel("This alert does not confirm a leak.");
        note.setForeground(Color.GRAY);
        note.setFont(new Font("Arial", Font.ITALIC, 12));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(50));
        panel.add(title);
        panel.add(Box.createVerticalStrut(35));
        panel.add(message);
        panel.add(Box.createVerticalStrut(20));
        panel.add(inspect);
        panel.add(Box.createVerticalStrut(25));
        panel.add(note);

        add(panel);
        setVisible(true);
    }

}
