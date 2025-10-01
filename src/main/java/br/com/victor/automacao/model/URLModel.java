package br.com.victor.automacao.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLModel extends Pesquisa{

    public URLModel(String titulo, String localizacao) {
        super(titulo, localizacao);
    }

    public String getUrl (){
        try {
            String cargo = URLEncoder.encode(getTitulo(), StandardCharsets.UTF_8.toString());
            String local = URLEncoder.encode(getLocalizacao(), StandardCharsets.UTF_8.toString());

            return String.format("https://br.indeed.com/jobs?q=%s&l=%s", cargo, local);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao codificar a url");
        }


    }

    public  String toString (){
        return getUrl();
    }

}
