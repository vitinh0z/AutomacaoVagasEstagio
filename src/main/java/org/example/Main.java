package org.example;

import br.com.victor.automacao.model.EnviarNotificaoDiscord;
import br.com.victor.automacao.model.GupyScraper;
import br.com.victor.automacao.model.RemotarScraper;
import br.com.victor.automacao.model.Vaga;
import br.com.victor.automacao.services.ScraperSite;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {


    private static final String ARQUIVO_VAGAS_ENVIADAS = "var/data/vagas_enviadas.txt";
    private static final long INTERVALO_ENTRE_VAGAS_MS = 4 * 60 * 1000; // 4 minutos

    public static void main(String[] args) {

        String discordUrl = System.getenv("DISCORD_URL");
        if (discordUrl== null || discordUrl.isEmpty()){
            System.err.println("Erro: A variavel de ambeinte DISCORD_URL NÃO FOI ENCONTRADA");
            return;
        }
        Queue<Vaga> filaDeVagasParaEnviar = new LinkedList<>();
        EnviarNotificaoDiscord notifier = new EnviarNotificaoDiscord(discordUrl);

        while (true) {
            if (filaDeVagasParaEnviar.isEmpty()) {

                System.out.println("Fila vazia. Buscando por novas vagas... - " + agora());


                Set<String> linksJaEnviados = carregarLinksEnviados();
                List<Vaga> vagasUnicas = buscarTodasAsVagas();

                List<Vaga> vagasNovas = vagasUnicas.stream()
                        .filter(vaga -> vaga.getLink() != null && !vaga.getLink().isEmpty())
                        .filter(vaga -> !linksJaEnviados.contains(vaga.getLink()))
                        .collect(Collectors.toList());

                if (vagasNovas.isEmpty()) {
                    System.out.println("Nenhuma vaga nova encontrada. Aguardando para buscar novamente...");
                } else {
                    filaDeVagasParaEnviar.addAll(vagasNovas);
                    System.out.println(vagasNovas.size() + " novas vagas foram adicionadas à fila de envio.");
                }
            }

            if (!filaDeVagasParaEnviar.isEmpty()) {
                Vaga vagaParaEnviar = filaDeVagasParaEnviar.poll();

                if (vagaParaEnviar != null && vagaParaEnviar.getLink() != null && !vagaParaEnviar.getLink().isEmpty()) {
                    System.out.println("\n[" + agora() + "] Enviando próxima vaga da fila para o Discord: " + vagaParaEnviar.getTitle());
                    notifier.notificar(vagaParaEnviar);
                    salvarLinkEnviado(vagaParaEnviar.getLink()); // Salva na "memória"

                    System.out.println("Vaga enviada. " + filaDeVagasParaEnviar.size() + " vagas restantes na fila.");
                } else {
                    System.out.println("\n[" + agora() + "] Vaga Invalida (sem link");
                }
                if (filaDeVagasParaEnviar.isEmpty()){
                    System.out.println("Lista Terminada. Todas Vagas foram enviadas");
                }
            }

            try {
                System.out.println("Próxima ação em 4 minutos...");
                Thread.sleep(INTERVALO_ENTRE_VAGAS_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    private static List<Vaga> buscarTodasAsVagas() {

        List<String> cargos = Arrays.asList("Estágio em Desenvolvimento de Software",
                "Estágio em Programação Back-End",
                "Estágio em Engenharia de Software",
                "Estágio em Ciência da Computação",
                "Estágio em Análise e Desenvolvimento de Sistemas",
                "Estágio em Desenvolvimento Web",
                "Estágio em Suporte Técnico em TI",
                "Estágio em Infraestrutura de Redes",
                "Estágio em Banco de Dados",
                "Estágio em Automação de Testes",
                "Estágio em DevOps",
                "Estágio em Segurança da Informação",
                "Estágio em Desenvolvimento Mobile",
                "Estágio em Tecnologia da Informação",
                "Estágio em Full Stack Development",

                "Trainee em Desenvolvimento de Software",
                "Trainee Desenvolvedor Java",
                "Trainee Back-End Developer",
                "Trainee em Engenharia de Software",
                "Trainee em Infraestrutura de TI",
                "Trainee em Cloud Computing",
                "Trainee em Análise de Sistemas",
                "Trainee Full Stack Developer",
                "Trainee em Cybersegurança",
                "Trainee em Ciência de Dados",
                "Trainee em Automação e QA",
                "Trainee em Desenvolvimento de Aplicações Web",
                "Trainee em TI");

        List<ScraperSite> scrapers = new ArrayList<>();
        scrapers.add(new RemotarScraper());
        scrapers.add(new GupyScraper());

        List<Vaga> todasAsVagas = new ArrayList<>();

        for (String cargo : cargos) {
            System.out.println("Buscando por: '" + cargo + "'");

            for (ScraperSite scraper : scrapers) {
                String url = scraper.buscarUrl(cargo, "");
                if (url == null || url.isEmpty()) continue;
                try {
                    Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").ignoreContentType(true).execute();
                    String jsonResponse = response.body();

                    List<Vaga> vagasDoSite = scraper.extrairVagas(jsonResponse);
                    if (vagasDoSite != null && !vagasDoSite.isEmpty()) {
                        System.out.println("\n>>>>>> Vagas recebidas antes do filtro: " + vagasDoSite.size());
                        List<Vaga> vagasFiltradas = filtrarVagas(vagasDoSite, cargo);
                        System.out.println("\n>>>>>> Vagas que passaram no filtro: " + vagasFiltradas.size());
                        todasAsVagas.addAll(vagasFiltradas);
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao buscar vagas para " + cargo + ": " + e.getMessage());
                }
            }
        }
        return new ArrayList<>(new LinkedHashSet<>(todasAsVagas));
    }


    private static String normalizarString(String texto) {
        if (texto == null) {
            return "";
        }

        String textoNormalizado = texto.toLowerCase();

        textoNormalizado = Normalizer.normalize(textoNormalizado, Normalizer.Form.NFD);
        textoNormalizado = textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        return textoNormalizado;
    }

    public static List<Vaga> filtrarVagas(List<Vaga> vagas, String cargo) {
        List<Vaga> vagasFiltradas = new ArrayList<>();

        String[] palavrasChave = normalizarString(cargo).split(" ");

        for (Vaga vaga : vagas) {
            if (vaga.getTitle() == null || vaga.getLink() == null || vaga.getLink().isEmpty()) continue;

            // Normaliza o título da vaga antes de comparar
            String tituloVaga = normalizarString(vaga.getTitle());


            boolean contemTodas = true;
            for (String palavra : palavrasChave) {
                if (!tituloVaga.contains(palavra)) {
                    contemTodas = false;
                    break;
                }
            }

            if (contemTodas) {
                vagasFiltradas.add(vaga);
            }
        }
        return vagasFiltradas;
    }



    private static Set<String> carregarLinksEnviados() {
        try {
            return new HashSet<>(Files.readAllLines(Paths.get(ARQUIVO_VAGAS_ENVIADAS)));
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    private static void salvarLinkEnviado(String link) {
        try {
            Files.write(Paths.get(ARQUIVO_VAGAS_ENVIADAS), (link + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String agora() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}