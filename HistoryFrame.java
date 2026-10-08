import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryFrame extends JFrame {

    public HistoryFrame() {

        setTitle("Usage History");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Previous Usage Records");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        String[] columns = {
            "Date",
            "Quantity",
            "Consumption"
        };

        Object[][] data = {
            {"20-09-2026", "22 L", "Normal"},
            {"21-09-2026", "24 L", "Normal"},
            {"22-09-2026", "23 L", "Normal"},
            {"23-09-2026", "40 L", "Abnormal"},
            {"24-09-2026", "25 L", "Normal"}
        };

        DefaultTableModel model =
                new DefaultTableModel(data, columns);

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout(10, 10));

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new HistoryFrame();
    }
}