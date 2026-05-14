package tetris;

import javax.swing.*;
// import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;


public class PainelJogo extends JPanel {
    
    private static int  COLUNAS = 15;
    private static int  LINHAS = 20;
    private static int TAM_CELULA_PX = 30;
    private static int QUEDAS_MS = 500;     
    private int  [][] tabuleiro = new int[LINHAS][COLUNAS];     
    private Tetromino pecaAtual;     
    private  int xPeça, yPeça;     
    private int pontosRodada = 0;
    private int[][] formatoAtual;

    private final Consumer<Integer> cbPontuacao;
    private final Consumer<Integer> cbGameOver;

    private final Timer timerQueda = new Timer(QUEDAS_MS, e -> passoQueda());

    public PainelJogo(Consumer<Integer> cbPontuacao, Consumer<Integer> cbGameOver) {
        this.cbPontuacao = cbPontuacao;
        this.cbGameOver = cbGameOver;

        setPreferredSize(new Dimension(COLUNAS * TAM_CELULA_PX,
                                        LINHAS * TAM_CELULA_PX ));

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

    public void reiniciar(){
        timerQueda.stop();
        iniciarNovaPartida();
    }

    private void iniciarNovaPartida(){
        for(int l = 0; l < LINHAS; l++)
            for(int c = 0; c < COLUNAS; c++)
                tabuleiro[l][c] = 0;
        pontosRodada = 0;
        cbPontuacao.accept(pontosRodada);

        novaPeca();

        timerQueda.start();

        requestFocusInWindow();

        repaint();
    }
    
    
    private void novaPeca(){
        pecaAtual = Tetromino.random();
        formatoAtual = pecaAtual.shape;

        xPeça = COLUNAS / 2 -2;
        yPeça = 0;
        if (!podeMover(xPeça,yPeça, formatoAtual)) {
            timerQueda.stop();
            cbGameOver.accept(pontosRodada);
        }
    }

    private void tecla(int code){
        switch(code){
            case KeyEvent.VK_LEFT -> mover(-1,0);
            case KeyEvent.VK_RIGHT -> mover(1,0);
            case KeyEvent.VK_DOWN -> mover(0,1);
            case KeyEvent.VK_UP -> rotacionar();
            case KeyEvent.VK_SPACE -> quedaInstantanea();
        }
    }

    private void mover(int dx, int dy){

        if(podeMover(xPeça + dx, yPeça + dy, formatoAtual)){
            xPeça += dx;
            yPeça += dy;

            repaint();
        }
    }

    private void rotacionar(){

        int[][] rot = pecaAtual.rotated();
        if (podeMover(xPeça, yPeça, rot)) {
            formatoAtual = rot;
            repaint();   
        }
    }
    

    private void quedaInstantanea(){
        while (podeMover(xPeça, yPeça + 1, formatoAtual)) {
            yPeça++; 
        }
        passoQueda();
    }

    private void passoQueda(){
        if (podeMover(xPeça, yPeça + 1, formatoAtual)) {
            yPeça++;
        }else{
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

    private boolean podeMover(int nx, int ny, int[][] mat){
        for(int r = 0; r < mat.length; r++)
            for(int c = 0; c < mat[0].length; c++)
                if (mat[r][c] != 0) {
                    int x = nx + c;
                    int y = ny + r;
                    if(x < 0 || x >= COLUNAS || y < 0 || y >= LINHAS) 
                        return false;
                }
        return true;
    }

    private void travarPeca() {
        int[][] m = formatoAtual;
        for (int r = 0; r < m.length; r++)
            for (int c = 0; c < m[0].length; c++)
                if (m[r][c] != 0) 
                    tabuleiro[yPeça + r][xPeça + c] = m[r][c];
    }

    private int limparLinhasCompletas(){
        int removidas = 0;
        for(int l = 0; l < LINHAS; l++) {
            boolean cheia = true;
            for(int c = 0; c < COLUNAS; c++)
                if (tabuleiro[l][c] == 0) {
                    cheia = false;
                    break;
                }
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
        
        for (int y = 0; y < LINHAS; y++) 
            for (int x = 0; x < COLUNAS; x++) 
                if (tabuleiro[y][x] != 0) 
                    desenharCelular(g, x, y, tabuleiro[y][x]);     
        int [][] mat = formatoAtual;
        for (int r = 0; r < mat.length; r++) 
            for (int c = 0; c < mat[0].length; c++) 
                if (mat[r][c] !=0) 
                    desenharCelular(g, xPeça + c, yPeça + r,  mat[r][c]);

        g.setColor(new Color(60, 60, 60)) ;  

        for (int x = 0; x <= COLUNAS; x++) 
            g.drawLine(x * TAM_CELULA_PX, 0,
                       x * TAM_CELULA_PX, LINHAS * TAM_CELULA_PX);

        for (int y = 0; y <= LINHAS ; y++) 
            g.drawLine(0, y * TAM_CELULA_PX,
                        COLUNAS * TAM_CELULA_PX, y * TAM_CELULA_PX);
    }

    private void desenharCelular(Graphics g, int cx, int cy, int idCor){
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
