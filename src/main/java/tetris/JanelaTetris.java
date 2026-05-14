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
    System.out.println("1 - fimDeJogo chamado");

    String nome = JOptionPane.showInputDialog(
        this,
        "Fim de Jogo! \n Digite seu nome para o ranking.",
        "Salvar Pontuação",
        JOptionPane.QUESTION_MESSAGE);

    System.out.println("2 - nome digitado: " + nome);

    if (nome == null || nome.isBlank()) nome = "Jogador";

    System.out.println("3 - antes do banco");

    try {
        PontuacaoDAO dao = PontuacaoDAO.getInstancia();
        System.out.println("4 - DAO criado");
        dao.salvarPontuacao(new ModeloPontuacao(nome, pontosObtidos));
        System.out.println("5 - pontuação salva");
        List<ModeloPontuacao> top10 = dao.obterTopPontuacaos(10);
        System.out.println("6 - top10 obtido");
        new DialogRanking(this, top10);
        System.out.println("7 - ranking exibido");
    } catch (Exception e) {
        System.out.println("ERRO: " + e.getMessage());
    }

    System.out.println("8 - antes do confirm");

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
