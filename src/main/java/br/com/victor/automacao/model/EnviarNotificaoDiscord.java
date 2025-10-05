package br.com.victor.automacao.model;

import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;

public class EnviarNotificaoDiscord {

    private final String webhookUrl;
    private final OkHttpClient client;

    public EnviarNotificaoDiscord(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.client = new OkHttpClient();
    }

    public void notificar(Vaga vaga) {
        String conteudo = formatarMensagem(vaga);



        JsonObject json = new JsonObject();
        json.addProperty("content", conteudo);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
        );


        Request request = new Request.Builder()
                .url(this.webhookUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erro ao enviar notificação para o Discord: " + response.code() + " - " + response.body().string());
            }
        } catch (IOException e) {
            System.err.println("Falha na requisição para o Discord: " + e.getMessage());
        }
    }


    public String formatarMensagem(Vaga vaga) {
        String descricao = vaga.getDescription();
        if (descricao.length() > 1500) {
            descricao = descricao.substring(0, 1500) + "...";
        }
        return String.format(
                """
                        **%s**
                        **Descrição:**
                        %s
                        **Link:**
                        %s""",
                vaga.getTitle(),
                descricao,
                vaga.getLink());
    }
}