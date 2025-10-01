package br.com.victor.automacao.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class URLModel extends Pesquisa{

    private Integer escolha;

    public Integer getEscolha() {
        return escolha;
    }


    public URLModel(String titulo, String localizacao) {
        super(titulo, localizacao);
    }

    public String getUrl (){
        try {
            String cargo = URLEncoder.encode(getTitulo(), StandardCharsets.UTF_8.toString());
            String local = URLEncoder.encode(getLocalizacao(), StandardCharsets.UTF_8.toString());

            List<String> list = new ArrayList<>();

                String remotar = String.format("https://remotar.com.br/search/jobs?q=%s+%s", cargo);

                String indeed = String.format("https://br.indeed.com/jobs?q=%s&l=%s", cargo, local);

                String jooble = String.format("https://br.jooble.org/SearchResult?ukw=%s&rgns=%s%2c", cargo, local);

                ((ArrayList<String>) list).add(remotar,indeed,jooble);





        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao codificar a url");
        }


    }

    public  String toString (){
        return getUrl();
    }

}
