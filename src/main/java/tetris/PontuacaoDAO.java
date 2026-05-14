package tetris;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class PontuacaoDAO {

    private static PontuacaoDAO instancia; 
    private final MongoCollection<Document> col; 
    private PontuacaoDAO(){

        MongoClient cli = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = cli.getDatabase("tetris");
        col = db.getCollection("pontuacoes");

    }
    public static PontuacaoDAO getInstancia() {
        if (instancia == null) 
            
            instancia = new PontuacaoDAO();
        
        return instancia;
        
    }

    public void salvarPontuacao(ModeloPontuacao mp){
        Document d = new Document("nome", mp.getNome())
                            .append("pontos", mp.getPontos())
                            .append("data", mp.getData());

        col.insertOne(d);
    }

    public List<ModeloPontuacao> obterTopPontuacaos(int n){

        List<ModeloPontuacao> lista = new ArrayList<>();

        col.find()

        .sort(new Document("pontos", -1).append("data", 1))

        .limit(n)

        .forEach(d ->
            lista.add(new ModeloPontuacao(
                d.getString("nome"),
                d.getInteger("pontos"),
                d.getDate("data")      ////////////////// ATENÇÃO ALTERAR PARA getString ou getDate
            ))

        );

    return lista;

    }

}

