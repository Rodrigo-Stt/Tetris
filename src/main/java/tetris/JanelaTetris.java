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

        setBackground(new Color(15, 15, 25));

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
    sidebar.setBackground(new Color(20, 20, 35));
    sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
    sidebar.setPreferredSize(new Dimension(160, 0));

    sidebar.add(criarSecao("PRÓXIMA", painelProximaPeca));
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSeparador());
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSecao("PONTUAÇÃO", criarValor(lblPontuacao)));
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSeparador());
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSecao("NÍVEL", criarValor(lblNivel)));
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSeparador());
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSecao("LINHAS", criarValor(lblLinhas)));
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSeparador());
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(criarSecao("CONTROLES", criarControles()));

    return sidebar;
    }

    private JPanel criarSecao(String titulo, JComponent conteudo) {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    p.setBackground(new Color(20, 20, 35));
    p.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel lbl = new JLabel(titulo);
    lbl.setForeground(new Color(120, 120, 150));
    lbl.setFont(new Font("Arial", Font.BOLD, 10));
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

    conteudo.setAlignmentX(Component.LEFT_ALIGNMENT);

    p.add(lbl);
    p.add(Box.createVerticalStrut(5));
    p.add(conteudo);

    return p;
    }

    private JSeparator criarSeparador() {
    JSeparator sep = new JSeparator();
    sep.setForeground(new Color(60, 60, 80));
    sep.setMaximumSize(new Dimension(130, 1));
    sep.setAlignmentX(Component.LEFT_ALIGNMENT);
    return sep;
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
            linha.setBackground(new Color(20, 20, 35));
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

        DialogFimDeJogo dialog = new DialogFimDeJogo(
        this, pontosObtidos, painelJogo.getNivel());

        try {
            PontuacaoDAO dao = PontuacaoDAO.getInstancia();
            dao.salvarPontuacao(new ModeloPontuacao(dialog.getNomeJogador(), pontosObtidos));
            List<ModeloPontuacao> top10 = dao.obterTopPontuacaos(10);
            new DialogRanking(this, top10);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível conectar ao banco de dados.\nRanking indisponível.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
        }

        if (dialog.isJogarNovamente()) {
            painelJogo.reiniciar();
        } else {
            System.exit(0);
        }
    }
}
