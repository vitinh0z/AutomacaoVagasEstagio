package org.example;


import br.com.victor.automacao.model.URLModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Cargo da vaga: ");
        String cargo = scanner.nextLine();

        System.out.println("Localização da Vaga: ");
        String localizacao = scanner.nextLine();

        URLModel urlModel = new URLModel(cargo,localizacao);
        String urlString = urlModel.getUrl();

        String userProxy = "eqqungmf";
        String passProxy = "dfov8o2fzsv6";
        String hostProxy = "142.111.48.253";
        String proxyPort = "7030";

        System.setProperty("http.proxyUser", userProxy);
        System.setProperty("http.proxyPassword", userProxy);

        System.setProperty("https.proxyUser", userProxy);
        System.setProperty("https.proxyPassword", userProxy);

        try {

           // precisa falar para o servidor que somos um navegador tentando acessar o site;

            Document document = Jsoup.connect(urlString).userAgent("Mozilla/5.0 ;(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                    .proxy(hostProxy, Integer.parseInt(proxyPort)).timeout(2000).get();


            Element tituloHTML = document.selectFirst(".jobTitle.jobTitle-newJob.mosaic-provider-jobcards-bl7gmb.eu4oa1w0");
            if (tituloHTML != null){
                String titulo = tituloHTML.text();
                System.out.println(titulo);
            }



            Element descricaoHTML = document.selectFirst("#jobDescriptionText"); // Descricao da Vaga
            if (descricaoHTML!= null){
                String descricao = descricaoHTML.text();
                System.out.println(descricao);
            }

        }catch (IOException e){
            System.out.println("Erro ao tentar Conectar a URL: " + urlString + " " + e.getMessage());
        }



    }
}
