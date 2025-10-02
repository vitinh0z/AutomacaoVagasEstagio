package br.com.victor.automacao.model;

import br.com.victor.automacao.services.ScraperSite;
import org.jsoup.nodes.Document;
import java.util.List;

public class IndeedScraper implements ScraperSite {

    @Override
    public String buscarUrl(String cargo, String localizacao) {
        return String.format("https://br.indeed.com/jobs?q=%s&l=%s", cargo, localizacao);
    }

    @Override
    public List<Vaga> extrairVagas(String jsonResponse) {
        return List.of();
    }

    @Override
    public List<Vaga> extrairVagas(Document document) {
        return List.of();
    }
}



