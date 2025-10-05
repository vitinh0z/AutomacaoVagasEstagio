package br.com.victor.automacao.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Vaga {

    private String title;
    private String description;
    private String name;

    @SerializedName("externalLink")

    private String link;

    public Vaga(String title, String description, String link, String name) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.name = name;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return  true;
        if (o == null || getClass() != o.getClass()) return false;
        Vaga vaga = (Vaga) o;
        return  Objects.equals(link, vaga.link);
    }

    public int hashCode(){
        return Objects.hash(link);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    @Override
    public String toString() {
        return "\n - Vaga: " + getTitle() + "\n  - Link: " + getLink() + "\n - Descrição: " + getDescription();
    }
}
