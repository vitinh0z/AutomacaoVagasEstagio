package org.example;


import br.com.victor.automacao.model.RemotarScraper;
import br.com.victor.automacao.model.Vaga;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



import java.io.IOException;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Cargo da vaga: ");
        String cargo = scanner.nextLine();

        System.out.println("Localização da Vaga: ");
        String localizacao = scanner.nextLine();

        RemotarScraper remotarScraper = new RemotarScraper();
        String urlRemotar = remotarScraper.buscarUrl(cargo,localizacao);


        try {

            System.out.println("Tentando conectar na URL: " + urlRemotar);
            Document document = Jsoup.connect(urlRemotar).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36").get();

             List<Vaga> vagasEncontradas = remotarScraper.extrairVagas(document);

             if (vagasEncontradas.isEmpty()){
                 System.out.println("Nenhuma vaga encontrada");
             }
             else {
                 System.out.println("----------------------------");

                 System.out.println(vagasEncontradas.size() + "vagas encontradas");

                 System.out.println("-----------------------------");
             }

             for (Vaga vaga : vagasEncontradas){
                 System.out.println(vaga);
                 System.out.println("------");
             }


        }catch (IOException e){
            System.out.println("Erro ao tentar Conectar a URL: " + urlRemotar + " " + e.getMessage());
        }



    }
}
