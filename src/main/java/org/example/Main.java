package org.example;


import br.com.victor.automacao.model.URLModel;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Titulo da vaga: ");
        String titulo = scanner.nextLine();

        System.out.println("Localização da Vaga: ");
        String localizacao = scanner.nextLine();

        URLModel url = new URLModel(titulo,localizacao);

        System.out.println(url.getUrl());

    }
}
