package org.example;

import br.com.victor.automacao.model.EnviarNotificaoDiscord;
import br.com.victor.automacao.model.RemotarScraper;
import br.com.victor.automacao.model.Vaga;
import br.com.victor.automacao.services.ScraperSite;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;

public class Main {

    public static final String DISCORD_URL = "https://discord.com/api/webhooks/1424145095006883911/N5i7Qpesg98pKAmkDsgrPDs73zxTTKf1KzgIZGqUPwYx6_CRRZzRzwqh7RO167JlSuO2";

    public static void main(String[] args) {



        Scanner scanner = new Scanner(System.in);

        List<String> cargos = Arrays.asList("Estagio Java", "Estagio Desenvolvedor BackEnd", "Estagio Desenvolvedor de Software", "Estagio TI");


        String localizacao = "";

        EnviarNotificaoDiscord enviarNotificaoDiscord = new EnviarNotificaoDiscord(DISCORD_URL);

        List<ScraperSite> scrapers = new ArrayList<>();
        scrapers.add(new RemotarScraper());

        List<Vaga> todasAsVagas = new ArrayList<>();
        for (String cargo : cargos) {
            System.out.println("Buscando pelo Cargo: " + cargos);

            for (ScraperSite scraper : scrapers) {
                String siteName = scraper.getClass().getSimpleName();
                System.out.println("\n--- Buscando vagas no site: " + siteName + " ---");

                String url = scraper.buscarUrl(cargo, localizacao);

                if (url == null || url.isEmpty()) {
                    System.out.println("URL não encontrada para " + siteName);
                    continue;
                }

                System.out.println("Conectando à URL: " + url);

                try {
                    Connection.Response response = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                            .ignoreContentType(true)
                            .execute();

                    String jsonResponse = response.body();

                    List<Vaga> vagasDoSite = scraper.extrairVagas(jsonResponse);

                    if (vagasDoSite != null && !vagasDoSite.isEmpty()) {
                        System.out.println(vagasDoSite.size() + " vagas encontradas em " + siteName);

                        List<Vaga> vagasFiltradas = filtrarVagas(vagasDoSite, cargo);

                        todasAsVagas.addAll(vagasFiltradas);

                    } else {
                        System.out.println("Nenhuma vaga encontrada em " + siteName);
                    }

                } catch (IOException e) {
                    System.out.println("Erro ao tentar conectar à URL para " + siteName + ": " + url);
                    System.err.println("Detalhes do erro: " + e.getMessage());
                }
            }
        }


            System.out.println("\n--- Processamento concluído ---");
            System.out.println("Total de vagas encontradas (com duplicatas): " + todasAsVagas.size());

            Set<Vaga> vagasUnicasSet = new LinkedHashSet<>(todasAsVagas);
            List<Vaga> vagasUnicas = new ArrayList<>(vagasUnicasSet);

            System.out.println("Total de vagas únicas: " + vagasUnicas.size());

            System.out.println("\n--- Lista de Vagas Únicas ---");
            if (vagasUnicas.isEmpty()) {
                System.out.println("Nenhuma vaga foi encontrada com os critérios informados.");
            } else {

                System.out.println("Enviando para o dicord");

                for (Vaga vaga : vagasUnicas){
                    System.out.println("Enviando vaga " + vaga.getTitle());
                    enviarNotificaoDiscord.notificar(vaga);

                }
                System.out.println("notificao enviadas com sucesso");


            }
    }


    public static List<Vaga> filtrarVagas (List<Vaga> vagas, String cargo){
        List<Vaga> vagasFiltradas = new ArrayList<>();
        String[] palavrasChave = cargo.toLowerCase().split(" ");

        for (Vaga vaga : vagas){
            String tituloVaga = vaga.getTitle().toLowerCase();
            boolean contemPalavra = true;

            for (String palavra : palavrasChave){
                if (!tituloVaga.contains(palavra)){
                    contemPalavra = false;
                    break;

                }
            }

            if (contemPalavra){
                vagasFiltradas.add(vaga);
            }

        }
        return vagasFiltradas;
    }

}
