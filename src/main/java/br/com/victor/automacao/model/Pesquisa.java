package br.com.victor.automacao.model;

public class Pesquisa  {
    private String titulo;
    private String localizacao;

    public Pesquisa(String titulo, String localizacao) {
        this.titulo = titulo;
        this.localizacao = localizacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }


}
