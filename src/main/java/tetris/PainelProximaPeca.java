package tetris;

import javax.swing.*;
import java.awt.*;

public class PainelProximaPeca extends JPanel {

    private Tetromino peca;

    public PainelProximaPeca() {
        setPreferredSize(new Dimension(120, 80));
        setMaximumSize(new Dimension(120, 80));
        setBackground(new Color(20, 20, 20));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void setPeca(Tetromino p) {
        this.peca = p;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (peca == null) return;

        int tam = 20;
        int[][] shape = peca.shape;
        int offsetX = (getWidth()  - shape[0].length * tam) / 2;
        int offsetY = (getHeight() - shape.length    * tam) / 2;

        for (int r = 0; r < shape.length; r++)
            for (int c = 0; c < shape[0].length; c++)
                if (shape[r][c] != 0) {
                    g.setColor(peca.color);
                    g.fillRect(offsetX + c * tam, offsetY + r * tam, tam, tam);
                    g.setColor(Color.BLACK);
                    g.drawRect(offsetX + c * tam, offsetY + r * tam, tam, tam);
                }
    }
}