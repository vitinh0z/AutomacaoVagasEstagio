package br.com.victor.automacao.model;

import br.com.victor.automacao.services.ScraperSite;
import com.google.gson.Gson;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Inherited;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemotarScraper implements ScraperSite {

    private static class ApiResponse{
        List<Vaga> data;
    }

        @Override
        public String buscarUrl (String cargos, String localizacao) {
        try {
            String cargoCodificado = URLEncoder.encode(cargos, StandardCharsets.UTF_8.name());

            return String.format("https://api.remotar.com.br/jobs?search=%s", cargoCodificado);
        }catch (UnsupportedEncodingException e){
            System.out.println("Erro ao Tentar codificar URL " + e.getMessage());
            return "";
        }
    }

    @Override
    public List<Vaga> extrairVagas(String jsonResponse) {

        if (jsonResponse == null || jsonResponse.isEmpty()){
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        ApiResponse response = gson.fromJson(jsonResponse, ApiResponse.class);

        if (response != null && response.data != null){
            return response.data;
        }


        return new ArrayList<>();
    }

    @Override
    public List<Vaga> extrairVagas(Document document) {
        return List.of();
    }
}




