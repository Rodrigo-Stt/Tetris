package tetris;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DialogRanking  extends JDialog{
    
    public  DialogRanking(JFrame owner, List<ModeloPontuacao > ranking) {
        super(owner, "Ranking", true);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-20s %s%n","Pos", "Nome", "Pontos" ));

        sb.append("___________________________________________________________ \n");

        int pos = 1;

        for(ModeloPontuacao mp : ranking) {

            sb.append(String.format("%-4s %-20s %s%n",
            pos++, mp.getNome(), mp.getPontos()));
        }

        JTextArea area = new JTextArea(sb.toString()); 

        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        area.setEditable(false);

        add(new JScrollPane(area));

        setSize(320, 300);

        setLocationRelativeTo(owner);

        setVisible(true);
    }
}

