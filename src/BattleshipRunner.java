import javax.swing.*;

public class BattleshipRunner {
    public static void main(String[] args) {
        try {
            // Cross-platform look so the cell colors show the same on Mac and Windows
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new BattleshipFrame().setVisible(true));
    }
}
