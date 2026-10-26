import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * One JButton cell of the 10x10 grid. Knows its row/column and draws its state:
 * a light blue wave for BLANK, a yellow M for MISS and a red X for HIT.
 * Fired cells are disabled so they no longer respond to clicks.
 */
public class CellButton extends JButton {
    private static final Color WATER = new Color(16, 52, 99);
    private static final Color WAVE = new Color(135, 206, 250);   // light blue
    private static final Color MISS_COLOR = new Color(255, 215, 0); // yellow
    private static final Color HIT_COLOR = new Color(230, 30, 30);  // red
    private static final int ICON_SIZE = 40;

    private static final Icon WAVE_ICON = makeIcon("~", WAVE);
    private static final Icon MISS_ICON = makeIcon("M", MISS_COLOR);
    private static final Icon HIT_ICON = makeIcon("X", HIT_COLOR);

    private final int row;
    private final int col;

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
        setFocusPainted(false);
        setOpaque(true);
        setBackground(WATER);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(48, 48));
        display(CellState.BLANK);
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    /** Updates the picture to match the cell state and disables fired cells. */
    public void display(CellState state) {
        Icon icon = switch (state) {
            case BLANK -> WAVE_ICON;
            case MISS -> MISS_ICON;
            case HIT -> HIT_ICON;
        };
        setIcon(icon);
        setDisabledIcon(icon); // keep the color when the button is disabled
        setEnabled(state == CellState.BLANK);
    }

    /** Draws a character in a color as an icon (so the color survives being disabled). */
    private static Icon makeIcon(String text, Color color) {
        BufferedImage img = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, text.equals("~") ? 34 : 28));
        g.setColor(color);
        FontMetrics fm = g.getFontMetrics();
        int x = (ICON_SIZE - fm.stringWidth(text)) / 2;
        int y = (ICON_SIZE - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
        g.dispose();
        return new ImageIcon(img);
    }
}
