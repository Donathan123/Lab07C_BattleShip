import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** The Swing GUI: title, 10x10 grid, status counters, Play Again and Quit buttons. */
public class BattleshipFrame extends JFrame {
    private final BattleshipGame game = new BattleshipGame();
    private final CellButton[][] cells = new CellButton[Board.SIZE][Board.SIZE];

    private final JLabel missLabel = new JLabel();
    private final JLabel strikeLabel = new JLabel();
    private final JLabel totalMissLabel = new JLabel();
    private final JLabel totalHitLabel = new JLabel();

    public BattleshipFrame() {
        super("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("BATTLESHIP", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        main.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(Board.SIZE, Board.SIZE, 2, 2));
        grid.setBackground(Color.DARK_GRAY);
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                CellButton cell = new CellButton(r, c);
                cell.addActionListener(e -> fire(cell));
                cells[r][c] = cell;
                grid.add(cell);
            }
        }
        main.add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        JPanel status = new JPanel(new GridLayout(2, 2, 4, 4));
        Font statusFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        for (JLabel label : new JLabel[]{missLabel, strikeLabel, totalMissLabel, totalHitLabel}) {
            label.setFont(statusFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            status.add(label);
        }
        bottom.add(status, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton playAgain = new JButton("Play Again");
        JButton quit = new JButton("Quit");
        playAgain.addActionListener(e -> confirmPlayAgain());
        quit.addActionListener(e -> confirmQuit());
        buttons.add(playAgain);
        buttons.add(quit);
        bottom.add(buttons, BorderLayout.SOUTH);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
        startNewGame();
        pack();
        setLocationRelativeTo(null);
    }

    private void startNewGame() {
        game.newGame();
        for (CellButton[] row : cells) {
            for (CellButton cell : row) {
                cell.display(CellState.BLANK);
            }
        }
        updateStatus();
        System.out.println("New game - ship layout (debug):");
        System.out.println(game.getBoard());
    }

    private void fire(CellButton cell) {
        ShotResult result = game.fire(cell.getRow(), cell.getCol());
        if (result == ShotResult.ALREADY_FIRED) {
            return;
        }
        cell.display(game.getBoard().getState(cell.getRow(), cell.getCol()));
        updateStatus();

        if (game.isWon()) {
            endGame("You sank all 5 ships - YOU WIN!", "Victory!");
        } else if (result == ShotResult.SUNK) {
            int size = game.getBoard().getShipAt(cell.getRow(), cell.getCol()).getSize();
            JOptionPane.showMessageDialog(this, "You sunk a ship! (size " + size + ")", "Sunk!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (game.isLost()) {
            endGame("3 strikes - you lose! The fleet got away.", "Game Over");
        }
    }

    private void endGame(String message, String title) {
        int choice = JOptionPane.showConfirmDialog(this, message + "\nPlay again?", title,
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            System.exit(0);
        }
    }

    private void confirmPlayAgain() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure? This will end the current game and start a new one.",
                "Play Again", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to play again instead of quitting?", "Quit",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void updateStatus() {
        GameStats s = game.getStats();
        missLabel.setText("Misses: " + s.getMissCount() + "/5");
        strikeLabel.setText("Strikes: " + s.getStrikeCount() + "/3");
        totalMissLabel.setText("Total Misses: " + s.getTotalMisses());
        totalHitLabel.setText("Total Hits: " + s.getTotalHits() + "/17");
    }
}
