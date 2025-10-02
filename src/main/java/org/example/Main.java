package org.example;

import br.com.victor.automacao.model.RemotarScraper;
import br.com.victor.automacao.model.Vaga;
import br.com.victor.automacao.services.ScraperSite;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Cargo da vaga: ");
        String cargo = scanner.nextLine();

        System.out.println("Localização da Vaga: ");
        String localizacao = scanner.nextLine();

        List<ScraperSite> scrapers = new ArrayList<>();
        scrapers.add(new RemotarScraper());

        List<Vaga> todasAsVagas = new ArrayList<>();

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
                    todasAsVagas.addAll(vagasDoSite);
                } else {
                    System.out.println("Nenhuma vaga encontrada em " + siteName);
                }

            } catch (IOException e) {
                System.out.println("Erro ao tentar conectar à URL para " + siteName + ": " + url);
                System.err.println("Detalhes do erro: " + e.getMessage());
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
            for (Vaga vaga : vagasUnicas) {
                System.out.println(vaga);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Vagas.txt", true))) {
                    writer.write(String.valueOf(vaga));

                } catch (IOException e) {
                    System.out.println("Erro ao tentar gravar arquivo");
                    e.printStackTrace();
                }
            }
        }


        scanner.close();

    }
}