package modelo;

public class Produto {
    private String nome;
    private String marca;
    private Double peso;
    private Double volume;
    private Integer quantidade;
    private Double preco;

    public Produto(String nome, String marca,Integer quantidade, Double preco) {
        this.nome = nome;
        this.marca = marca;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getNome() {
        return this.nome;
    }

    public String getMarca() {
        return this.marca;
    }

    public Double getPeso() {
        return this.peso;
    }

    public Double getVolume() {
        return this.volume;
    }

    public Integer getQuantidade() {
        return this.quantidade;
    }

    public Double getPreco() {
        return this.preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }


}
