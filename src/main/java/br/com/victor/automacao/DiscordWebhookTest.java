package br.com.victor.automacao;

import okhttp3.*;
import com.google.gson.JsonObject;

import java.io.IOException;

public class DiscordWebhookTest {

    // 🔗 Cole aqui o SEU webhook (ex: https://discord.com/api/webhooks/...)
    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1424145095006883911/N5i7Qpesg98pKAmkDsgrPDs73zxTTKf1KzgIZGqUPwYx6_CRRZzRzwqh7RO167JlSuO2";
    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        enviarMensagemDiscord("🚀 Teste de envio automático!\nMensagem simples pra validar webhook.");
    }

    public static void enviarMensagemDiscord(String mensagem) {
        // Cria o JSON usando Gson (sem necessidade de replace manual)
        JsonObject json = new JsonObject();
        json.addProperty("content", mensagem);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(WEBHOOK_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Código de resposta: " + response.code());
            System.out.println("Mensagem: " + response.message());

            // Mostra os cabeçalhos de rate limit
            System.out.println("X-RateLimit-Remaining: " + response.header("x-ratelimit-remaining"));
            System.out.println("X-RateLimit-Reset-After: " + response.header("x-ratelimit-reset-after"));

            if (response.code() == 204) {
                System.out.println("✅ Mensagem enviada com sucesso (sem corpo de resposta).");
            } else if (response.isSuccessful()) {
                System.out.println("✅ Mensagem enviada e confirmada: " + response.body().string());
            } else {
                System.err.println("❌ Falha ao enviar mensagem. Resposta: " + response.body().string());
            }

        } catch (IOException e) {
            System.err.println("💥 Erro de conexão: " + e.getMessage());
        }
    }
}