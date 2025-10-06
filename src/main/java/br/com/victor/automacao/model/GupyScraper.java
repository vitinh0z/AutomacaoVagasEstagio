package br.com.victor.automacao.model;

import br.com.victor.automacao.services.ScraperSite;
import com.google.gson.Gson;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GupyScraper implements ScraperSite {

    public static class ApiResponse {
        List<Vaga> data;
    }


    @Override
    public String buscarUrl(String cargo, String localizacao) {
        try {

            String cargoCodificado = URLEncoder.encode(cargo, StandardCharsets.UTF_8.name());
        
            return String.format("https://employability-portal.gupy.io/api/v1/jobs?jobName=%s&offset=0", cargoCodificado);

        } catch (UnsupportedEncodingException e){

            System.out.println("erro ao tentar codificar a URL " + e.getMessage());
            return"";
        }
    }




    @Override
    public List<Vaga> extrairVagas(String jsonResponse) {

        if (jsonResponse == null || jsonResponse.isEmpty()) {
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




