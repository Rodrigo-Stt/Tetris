package tetris;

import java.util.Date;


public class ModeloPontuacao {

    private final String nome;
    private final int pontos;
    private final Date data; 

    public ModeloPontuacao(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
        this.data = new Date();
    }


    public String getNome() {
        return nome;
    }


    public int getPontos() {
        return pontos;
    }


    public Date getData() {
        return data;
    }
    
}


