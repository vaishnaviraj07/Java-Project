import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter today's water consumption (L):");
        if (input == null) return;

        try {
            double usage = Double.parseDouble(input);
            new Report(usage);
            if (usage > 25) new Alert();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a number.");
        }
    }
}
