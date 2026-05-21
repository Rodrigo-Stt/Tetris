package tetris;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogFimDeJogo extends JDialog {

    private String nomeJogador;
    private boolean jogarNovamente = false;

    public DialogFimDeJogo(JFrame owner, int pontos, int nivel) {
        super(owner, "Fim de Jogo", true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(20, 20, 35));
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 120), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel lblTitulo = new JLabel("FIM DE JOGO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(80, 80, 120));
        sep.setMaximumSize(new Dimension(220, 1));

        JLabel lblPontosLabel = new JLabel("PONTUAÇÃO");
        lblPontosLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblPontosLabel.setForeground(new Color(120, 120, 150));
        lblPontosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPontos = new JLabel(String.valueOf(pontos));
        lblPontos.setFont(new Font("Arial", Font.BOLD, 48));
        lblPontos.setForeground(new Color(150, 200, 255));
        lblPontos.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNivelLabel = new JLabel("NÍVEL ATINGIDO");
        lblNivelLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblNivelLabel.setForeground(new Color(120, 120, 150));
        lblNivelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNivel = new JLabel(String.valueOf(nivel));
        lblNivel.setFont(new Font("Arial", Font.BOLD, 32));
        lblNivel.setForeground(Color.WHITE);
        lblNivel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNomeLabel = new JLabel("SEU NOME");
        lblNomeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblNomeLabel.setForeground(new Color(120, 120, 150));
        lblNomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campoNome = new JTextField("Jogador");
        campoNome.setMaximumSize(new Dimension(220, 35));
        campoNome.setFont(new Font("Arial", Font.PLAIN, 16));
        campoNome.setBackground(new Color(30, 30, 50));
        campoNome.setForeground(Color.WHITE);
        campoNome.setCaretColor(Color.WHITE);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 120), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        campoNome.setHorizontalAlignment(JTextField.CENTER);
        campoNome.selectAll();

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoes.setBackground(new Color(20, 20, 35));
        painelBotoes.setMaximumSize(new Dimension(220, 50));
        painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSim = criarBotao("JOGAR NOVAMENTE", new Color(50, 100, 180));
        JButton btnNao = criarBotao("SAIR", new Color(80, 40, 40));

        btnSim.addActionListener(e -> {
            nomeJogador = campoNome.getText().isBlank() ? "Jogador" : campoNome.getText();
            jogarNovamente = true;
            dispose();
        });

        btnNao.addActionListener(e -> {
            nomeJogador = campoNome.getText().isBlank() ? "Jogador" : campoNome.getText();
            jogarNovamente = false;
            dispose();
        });

        painelBotoes.add(btnSim);
        painelBotoes.add(btnNao);

        painel.add(lblTitulo);
        painel.add(Box.createVerticalStrut(15));
        painel.add(sep);
        painel.add(Box.createVerticalStrut(20));
        painel.add(lblPontosLabel);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblPontos);
        painel.add(Box.createVerticalStrut(15));
        painel.add(lblNivelLabel);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblNivel);
        painel.add(Box.createVerticalStrut(20));
        painel.add(lblNomeLabel);
        painel.add(Box.createVerticalStrut(5));
        painel.add(campoNome);
        painel.add(Box.createVerticalStrut(25));
        painel.add(painelBotoes);

        add(painel);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(105, 45));
        return btn;
    }

    public String getNomeJogador() { return nomeJogador; }
    public boolean isJogarNovamente() { return jogarNovamente; }
}