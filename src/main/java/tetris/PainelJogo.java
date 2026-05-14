package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class PainelJogo extends JPanel {

    private static final int COLUNAS = 10;
    private static final int LINHAS = 20;
    private static final int TAM_CELULA_PX = 30;
    private static final int QUEDAS_MS = 500;

    private int[][] tabuleiro = new int[LINHAS][COLUNAS];
    private Tetromino pecaAtual;
    private int xPeça, yPeça;
    private int pontosRodada = 0;
    private int[][] formatoAtual;
    // Guarda o índice da peça atual para usar a cor correta
    private int idCorAtual;

    private final Consumer<Integer> cbPontuacao;
    private final Consumer<Integer> cbGameOver;

    private final Timer timerQueda = new Timer(QUEDAS_MS, e -> passoQueda());

    public PainelJogo(Consumer<Integer> cbPontuacao, Consumer<Integer> cbGameOver) {
        this.cbPontuacao = cbPontuacao;
        this.cbGameOver = cbGameOver;

        setPreferredSize(new Dimension(COLUNAS * TAM_CELULA_PX, LINHAS * TAM_CELULA_PX));
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tecla(e.getKeyCode());
            }
        });

        iniciarNovaPartida();
    }

    public void reiniciar() {
        timerQueda.stop();
        iniciarNovaPartida();
    }

    private void iniciarNovaPartida() {
        for (int l = 0; l < LINHAS; l++)
            for (int c = 0; c < COLUNAS; c++)
                tabuleiro[l][c] = 0;
        pontosRodada = 0;
        cbPontuacao.accept(pontosRodada);
        novaPeca();
        timerQueda.start();
        requestFocusInWindow();
        repaint();
    }

    private void novaPeca() {
        pecaAtual = Tetromino.random();
        // Clona o shape para poder rotacionar independentemente
        formatoAtual = clonarMatriz(pecaAtual.shape);
        // Guarda o índice da peça para usar a cor correta (1 a 7)
        idCorAtual = java.util.Arrays.asList(Tetromino.values()).indexOf(pecaAtual) + 1;

        xPeça = COLUNAS / 2 - 2;
        yPeça = 0;

        if (!podeMover(xPeça, yPeça, formatoAtual)) {
            timerQueda.stop();
            cbGameOver.accept(pontosRodada);
        }
    }

    private int[][] clonarMatriz(int[][] origem) {
        int[][] copia = new int[origem.length][origem[0].length];
        for (int i = 0; i < origem.length; i++)
            System.arraycopy(origem[i], 0, copia[i], 0, origem[0].length);
        return copia;
    }

    private void tecla(int code) {
        switch (code) {
            case KeyEvent.VK_LEFT  -> mover(-1, 0);
            case KeyEvent.VK_RIGHT -> mover(1, 0);
            case KeyEvent.VK_DOWN  -> mover(0, 1);
            case KeyEvent.VK_UP    -> rotacionar();
            case KeyEvent.VK_SPACE -> quedaInstantanea();
        }
    }

    private void mover(int dx, int dy) {
        if (podeMover(xPeça + dx, yPeça + dy, formatoAtual)) {
            xPeça += dx;
            yPeça += dy;
            repaint();
        }
    }

    private void rotacionar() {
        int rows = formatoAtual.length;
        int cols = formatoAtual[0].length;
        int[][] rot = new int[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                rot[c][rows - 1 - r] = formatoAtual[r][c];

        if (podeMover(xPeça, yPeça, rot)) {
            formatoAtual = rot;
            repaint();
        }
    }

    private void quedaInstantanea() {
        while (podeMover(xPeça, yPeça + 1, formatoAtual))
            yPeça++;
        passoQueda();
    }

    private void passoQueda() {
        if (podeMover(xPeça, yPeça + 1, formatoAtual)) {
            yPeça++;
        } else {
            travarPeca();
            int linhas = limparLinhasCompletas();
            if (linhas > 0) {
                pontosRodada += linhas;
                cbPontuacao.accept(pontosRodada);
            }
            novaPeca();
        }
        repaint();
    }

    private boolean podeMover(int nx, int ny, int[][] mat) {
        for (int r = 0; r < mat.length; r++)
            for (int c = 0; c < mat[0].length; c++)
                if (mat[r][c] != 0) {
                    int x = nx + c;
                    int y = ny + r;
                    // Verifica limites do tabuleiro
                    if (x < 0 || x >= COLUNAS || y < 0 || y >= LINHAS)
                        return false;
                    // Verifica colisão com peças já travadas
                    if (tabuleiro[y][x] != 0)
                        return false;
                }
        return true;
    }

    private void travarPeca() {
        for (int r = 0; r < formatoAtual.length; r++)
            for (int c = 0; c < formatoAtual[0].length; c++)
                if (formatoAtual[r][c] != 0)
                    // Grava o idCor no tabuleiro para preservar a cor da peça
                    tabuleiro[yPeça + r][xPeça + c] = idCorAtual;
    }

    private int limparLinhasCompletas() {
        int removidas = 0;
        for (int l = 0; l < LINHAS; l++) {
            boolean cheia = true;
            for (int c = 0; c < COLUNAS; c++)
                if (tabuleiro[l][c] == 0) { cheia = false; break; }
            if (cheia) {
                removidas++;
                for (int y = l; y > 0; y--)
                    System.arraycopy(tabuleiro[y - 1], 0, tabuleiro[y], 0, COLUNAS);
                for (int c = 0; c < COLUNAS; c++)
                    tabuleiro[0][c] = 0;
            }
        }
        return removidas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha peças travadas no tabuleiro
        for (int y = 0; y < LINHAS; y++)
            for (int x = 0; x < COLUNAS; x++)
                if (tabuleiro[y][x] != 0)
                    desenharCelula(g, x, y, tabuleiro[y][x]);

        // Desenha peça atual
        for (int r = 0; r < formatoAtual.length; r++)
            for (int c = 0; c < formatoAtual[0].length; c++)
                if (formatoAtual[r][c] != 0)
                    desenharCelula(g, xPeça + c, yPeça + r, idCorAtual);

        // Grade
        g.setColor(new Color(60, 60, 60));
        for (int x = 0; x <= COLUNAS; x++)
            g.drawLine(x * TAM_CELULA_PX, 0, x * TAM_CELULA_PX, LINHAS * TAM_CELULA_PX);
        for (int y = 0; y <= LINHAS; y++)
            g.drawLine(0, y * TAM_CELULA_PX, COLUNAS * TAM_CELULA_PX, y * TAM_CELULA_PX);
    }

    private void desenharCelula(Graphics g, int cx, int cy, int idCor) {
        Tetromino[] pecas = Tetromino.values();
        Color cor = pecas[(idCor - 1) % pecas.length].color;

        int px = cx * TAM_CELULA_PX;
        int py = cy * TAM_CELULA_PX;

        g.setColor(cor);
        g.fillRect(px, py, TAM_CELULA_PX, TAM_CELULA_PX);
        g.setColor(Color.BLACK);
        g.drawRect(px, py, TAM_CELULA_PX, TAM_CELULA_PX);
    }
}