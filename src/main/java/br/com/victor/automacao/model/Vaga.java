package br.com.victor.automacao.model;

import com.google.gson.annotations.SerializedName;

public class Vaga {

    private String title;
    private String description;

    @SerializedName("externalLink")
    private String link;

    public Vaga(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (description != null){
            return org.jsoup.Jsoup.parse(description).text();
        }
        return "Descricao Não Fornecida";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Vaga: " + getTitle() + "\n  - Link: " + getLink() + "\n" + "Descrição: " + getDescription();
    }
}
