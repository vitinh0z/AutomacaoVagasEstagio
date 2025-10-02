package br.com.victor.automacao.services;
import br.com.victor.automacao.model.Vaga;
import org.jsoup.nodes.Document;
import java.util.List;

public interface ScraperSite {

    String buscarUrl(String cargo, String localizacao);

    List<Vaga> extrairVagas (String jsonResponse);

    List<Vaga> extrairVagas(Document document);
}
