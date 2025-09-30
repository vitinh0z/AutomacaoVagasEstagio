package org.example;

import java.io.IOException;

public class TesteConexao {

    public static void main (String[] args){

        String url = "https://www.google.com.br";

        try{
            System.out.println("Conectando a URL: " + url);

        } catch (IOException e){
            System.out.println("Erro ao tentar conectar: " + e.getMessage());
        }
// salvaaa


    }
}
