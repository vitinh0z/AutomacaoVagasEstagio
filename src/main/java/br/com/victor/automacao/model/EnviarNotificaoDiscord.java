package br.com.victor.automacao.model;


import okhttp3.*;


import java.io.IOException;
import java.util.*;

public class EnviarNotificaoDiscord {

    private final String webhookUrl;
    private final OkHttpClient client;

    public EnviarNotificaoDiscord(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.client = new OkHttpClient();
    }

    public void notificar(Vaga vaga) {
        String conteudo = formatarMensagem(vaga);

        String json = "{\"content\" :\"" + conteudo.replace("\"", "\\\"") + "\"}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(this.webhookUrl).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erro ao enviar notificacao para o discord: " + response.body().string());
            }
        } catch (IOException e) {
            System.err.println("Falha na requisição para o discord: " + e.getMessage());
        }
    }

    public String formatarMensagem(Vaga vaga) {

        String descricao = vaga.getDescription();

        if (descricao.length() > 1500){
            descricao = descricao.substring(0, 1500) + "...";
        }

        return String.format(
                """
                        **%s** \
                        **Descrição:**
                        %s\
                        **Link:**
                        %s""",
                vaga.getTitle(),
                descricao,
                vaga.getLink());
    }
}



