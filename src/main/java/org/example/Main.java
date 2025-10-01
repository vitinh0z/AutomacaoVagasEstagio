package org.example;


import br.com.victor.automacao.model.URLModel;


import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Titulo da vaga: ");
        String titulo = scanner.nextLine();

        System.out.println("Localização da Vaga: ");
        String localizacao = scanner.nextLine();

        URLModel url = new URLModel(titulo,localizacao);

        try {

            Document document = Jsoup.connect(url).get();

            Elements jobsCards = document.select()
            List<Document> list = new ArrayList<>();

            document.select("class= jobTitle jobTitle-newJob mosaic-provider-jobcards-bl7gmb eu4oa1w0").forEach(System.out.println()); // Titulo da vaga
            document.select("id=\"jobDescriptionText\"").forEach(System.out.println()); // Descricao da Vaga

           list.add(document);

        }catch (IOException e){
            System.out.println("Erro ao tentar Conectar a URL: " + url + " " + e.getMessage());
        }

    }
}
