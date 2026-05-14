package tetris;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JanelaTetris extends JFrame {

    private final JLabel lblPontuacao = new JLabel();
    private final PainelJogo painelJogo;

    public JanelaTetris() {
        super("Tetris - Pontuação em MongoDB");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        lblPontuacao.setFont(new Font("Arial", Font.BOLD, 18));
        barra.add(lblPontuacao);
        add(barra, BorderLayout.NORTH);

        painelJogo = new PainelJogo(this::atualizarPontuacao, this::fimDeJogo);
        add(painelJogo, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        painelJogo.requestFocusInWindow();
    }

    private void atualizarPontuacao(int pontos) {
        lblPontuacao.setText("Pontuação da Rodada: " + pontos);
    }

    private void fimDeJogo(int pontosObtidos) {
        String nome = JOptionPane.showInputDialog(
            this,
            "Fim de Jogo! \n Digite seu nome para o ranking.",
            "Salvar Pontuação",
            JOptionPane.QUESTION_MESSAGE);

        if (nome == null || nome.isBlank()) nome = "Jogador";

        PontuacaoDAO dao = PontuacaoDAO.getInstancia();
        dao.salvarPontuacao(new ModeloPontuacao(nome, pontosObtidos));

        List<ModeloPontuacao> top10 = dao.obterTopPontuacaos(10);
        new DialogRanking(this, top10);

        int opc = JOptionPane.showConfirmDialog(
            this,
            "Deseja jogar novamente?",
            "Novo jogo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (opc == JOptionPane.YES_OPTION) {
            painelJogo.reiniciar();
        } else {
            System.exit(0);
        }
    }
}
