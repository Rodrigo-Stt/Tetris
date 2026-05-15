package tetris;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class JanelaTetris extends JFrame {

    private final PainelJogo painelJogo;
    private final JLabel lblPontuacao = new JLabel("0");
    private final JLabel lblNivel = new JLabel("1");
    private final JLabel lblLinhas = new JLabel("0");
    private final PainelProximaPeca painelProximaPeca = new PainelProximaPeca();

    public JanelaTetris() {
        super("Tetris - MongoDB");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        painelJogo = new PainelJogo(this::atualizarPontuacao, this::fimDeJogo, this::atualizarSidebar);
        add(painelJogo, BorderLayout.CENTER);
        add(criarSidebar(), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        painelJogo.requestFocusInWindow();
        atualizarSidebar();
    }

    private void atualizarSidebar() {
        if (painelJogo == null) return;
        lblNivel.setText(String.valueOf(painelJogo.getNivel()));
        lblLinhas.setText(String.valueOf(painelJogo.getLinhasEliminadas()));
        painelProximaPeca.setPeca(painelJogo.getProximaPeca());
    }

    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(72, 104, 217));
        sidebar.setBorder(new EmptyBorder(30, 30, 30, 30));
        sidebar.setPreferredSize(new Dimension(160, 0));

        // Próxima peça
        sidebar.add(criarTitulo("PRÓXIMA PEÇA"));
        sidebar.add(Box.createVerticalStrut(9));
        sidebar.add(painelProximaPeca);
        sidebar.add(Box.createVerticalStrut(20));

        // Pontuação
        sidebar.add(criarTitulo("PONTUAÇÃO"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(criarValor(lblPontuacao));
        sidebar.add(Box.createVerticalStrut(20));

        // Nível
        sidebar.add(criarTitulo("NÍVEL"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(criarValor(lblNivel));
        sidebar.add(Box.createVerticalStrut(20));

        // Linhas
        sidebar.add(criarTitulo("LINHAS"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(criarValor(lblLinhas));
        sidebar.add(Box.createVerticalStrut(20));

        // Controles
        sidebar.add(criarTitulo("CONTROLES"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(criarControles());

        return sidebar;
    }

    private JLabel criarTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel criarValor(JLabel lbl) {
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarControles() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(72, 104, 217));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[][] controles = {
            {"<  >", "Mover"},
            {"▲", "Rotacionar"},
            {"▼", "Acelerar"},
            {"SPACE", "Queda"}
        };

        for (String[] c : controles) {
            JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
            linha.setBackground(new Color(72, 104, 217));
            linha.setMaximumSize(new Dimension(160, 25));

            JLabel tecla = new JLabel(c[0] + " ");
            tecla.setForeground(new Color(0, 245, 255));
            tecla.setFont(new Font("Arial", Font.BOLD, 12));

            JLabel acao = new JLabel(c[1]);
            acao.setForeground(new Color(200, 200, 200));
            acao.setFont(new Font("Arial", Font.PLAIN, 12));

            linha.add(tecla);
            linha.add(acao);
            p.add(linha);
        }

        return p;
    }

    private void atualizarPontuacao(int pontos) {
    if (painelJogo == null) return;
    lblPontuacao.setText(String.valueOf(pontos));
    lblNivel.setText(String.valueOf(painelJogo.getNivel()));
    lblLinhas.setText(String.valueOf(painelJogo.getLinhasEliminadas()));
    painelProximaPeca.setPeca(painelJogo.getProximaPeca());
    }

    private void fimDeJogo(int pontosObtidos) {
        String nome = JOptionPane.showInputDialog(
            this,
            "Fim de Jogo!\nDigite seu nome para o ranking.",
            "Salvar Pontuação",
            JOptionPane.QUESTION_MESSAGE);

        if (nome == null || nome.isBlank()) nome = "Jogador";

        try {
            PontuacaoDAO dao = PontuacaoDAO.getInstancia();
            dao.salvarPontuacao(new ModeloPontuacao(nome, pontosObtidos));
            List<ModeloPontuacao> top10 = dao.obterTopPontuacaos(10);
            new DialogRanking(this, top10);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível conectar ao banco de dados.\nRanking indisponível.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
        }

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
