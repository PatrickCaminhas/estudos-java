package modelo;
import java.math.BigDecimal;

public class Produto {
    private String nome;
    private String marca;
    private BigDecimal peso;
    private BigDecimal volume;
    private Integer quantidade;
    private BigDecimal preco;

    public Produto(String nome, String marca,Integer quantidade, BigDecimal preco) {
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

    public BigDecimal getPeso() {
        return this.peso;
    }

    public BigDecimal getVolume() {
        return this.volume;
    }

    public Integer getQuantidade() {
        return this.quantidade;
    }

    public BigDecimal getPreco() {
        return this.preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }


}
