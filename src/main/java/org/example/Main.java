package org.example;


import br.com.victor.automacao.model.RemotarScraper;
import br.com.victor.automacao.model.Vaga;
import br.com.victor.automacao.services.ScraperSite;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static String url;
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Cargo da vaga: ");
        String cargo = scanner.nextLine();

        System.out.println("Localização da Vaga: ");
        String localizacao = scanner.nextLine();

        RemotarScraper remotarScraper = new RemotarScraper();
        String urlRemotar = remotarScraper.buscarUrl(cargo,localizacao);



        List<ScraperSite> list = new ArrayList<>();

        list.add(new RemotarScraper());

        List<Vaga> todasAsVagas = new ArrayList<>();


        for(ScraperSite x : list){


            System.out.println("Buscando Vaga na URL: " + url);
            url = x.buscarUrl(cargo, localizacao);
        }


        try {

            System.out.println("Conectado com Sucesso na URL: " + url);
            Connection.Response response = Jsoup.connect(url).userAgent("Mozzila/5.0 Windows NT 10.0; Win64; x64) AppleWebJit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36").ignoreContentType(true).execute();

        }
        catch (IOException e){
            System.out.println("Erro ao tentar Conectar a URL: " + urlRemotar + " " + e.getMessage());
            e.printStackTrace();
        }



    }
}
