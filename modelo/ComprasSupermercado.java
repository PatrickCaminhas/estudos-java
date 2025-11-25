package modelo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.util.Scanner;

public class ComprasSupermercado {
    private Integer id = 1;
    private Date data;
    private String nomeSupermercado;
    private List<Produto> itens;
    private Util util;

    public ComprasSupermercado(String nomeSupermercado) {
        this.nomeSupermercado = nomeSupermercado;
        this.itens = new ArrayList<>();
        this.util = new Util();
    }

    public Date getData() {
        return this.data;
    }

    public String getSupermercado() {
        return this.nomeSupermercado;
    }

    public void adicionarProduto(String nome, String marca, Integer quantidade, Double preco) {
        Produto produto = new Produto(nome, marca, quantidade, preco);
        this.itens.add(produto);
    }

    public void removerProduto(int indice) {
        this.itens.remove(indice);
    }

    public List<Produto> getListaDeProdutos() {
        return this.itens;
    }

    public Double getTotalCompra() {
        Double total = 0.0;
        for (Produto produto : this.itens) {
            total += produto.getPreco()
                    * ((produto.getQuantidade() != null || produto.getQuantidade() != 0) ? produto.getQuantidade() : 1);
        }
        return total;
    }

    public Integer getQuantidadeItens() {
        return this.itens.size();
    }

    public void SalvarCompra() {
        String arquivo = criarGastosDoDia().toString() + "/compra-" + geraHashAleatoria(4) + ".txt";
        Path caminho = Paths.get(arquivo);
        String cabecalho = "Data;Supermercado;Quantidade de Itens;Total\n"
                + LocalDate.now() + ";" + this.nomeSupermercado + ";" + this.getQuantidadeItens() + ";"
                + getTotalCompra() + "\n" +
                "Marca;Produto;Peso;Volume;Quantidade;Preco Unitario,Total Produto\n";
        for (Produto produto : this.itens) {
            cabecalho += produto.getMarca() + ";" + produto.getNome() + ";" + produto.getPeso() + ";"
                    + produto.getVolume() + ";" + produto.getQuantidade() + ";" + produto.getPreco() + ";"
                    + (produto.getQuantidade() * produto.getPreco()) + ";" + "\n";
        }
        try {
            Files.writeString(caminho, cabecalho);
            System.out.println("Arquivo criado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Path criarGastosDoDia() {
        Path caminho = Paths.get("dados/gastos/" + LocalDate.now());
        try {
            Files.createDirectories(caminho);
        } catch (Exception e) {
            System.out.println("Erro ao criar pasta da data atual");

        }
        return caminho;
    }

    private String geraHashAleatoria(Integer tamanho) {
        String uuid = UUID.randomUUID().toString();
        String hashCurto = "";
        if (tamanho > 4) {
            hashCurto = uuid.substring(0, tamanho);
        } else {
            hashCurto = uuid.substring(0, 4);

        }
        return hashCurto;
    }

    public void menu() {
        this.util.limparTerminal();
        int opcao = -1;
        Scanner leitor = new Scanner(System.in);

        do {
            System.out.println(
                    "AÇÕES\n1- Adicionar Produto\n2- Remover Produto\n3- Lista de produtos\n4- Verificar Valor Total\n5- Salvar compra\n6- Sair");
            opcao = Integer.valueOf(leitor.nextLine());
            this.util.limparTerminal();

            switch (opcao) {
                case 1:
                    Boolean continuar = true;
                    do {
                        System.out.println("Digite o nome do produto");
                        String produto = leitor.nextLine();
                        System.out.println("\nDigite o nome da marca");
                        String marca = leitor.nextLine();
                        System.out.println("Digite a quantidade do produto");
                        String quantidade = leitor.nextLine();
                        System.out.println("Digite o preco do produto");
                        String preco = leitor.nextLine();

                        try {

                            Integer qtd = Integer.valueOf(quantidade);
                            Double pr = Double.valueOf(preco);
                            adicionarProduto(produto, marca, qtd, pr);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Quantidade ou preço inválido. Produto não adicionado.");
                        }
                        String resposta = "";
                                                    this.util.limparTerminal();

                        do {

                            System.out.println("Deseja adicionar mais algum produto?\nS - Sim\nN - Não");
                            resposta = leitor.nextLine();
                            if (resposta.equalsIgnoreCase("N")) {
                                continuar = false;
                            }
                            this.util.limparTerminal();

                        } while (!resposta.equalsIgnoreCase("S") && !resposta.equalsIgnoreCase("N"));

                    } while (continuar);
                    break;
                case 2:
                    this.listarProdutos();
                    System.out.println("Digite o numero do produto que queira remover da lista ");
                    int indice = Integer.valueOf(leitor.nextLine());
                    try {
                        this.removerProduto(indice);
                    } catch (Exception e) {
                        System.out.println("Produto não cadastrado. Insira outra opção!");
                    }
                    break;

                case 3:

                    this.listarProdutos();
                    leitor.nextLine();
                    break;
                case 4:
                    System.out.println("O total da compra é de R$" + this.getTotalCompra());
                    leitor.nextLine();

                    break;
                case 5:
                    this.SalvarCompra();
                    System.out.println("Compra salva em arquivo!");
                    leitor.nextLine();
                    break;
                case 6:
                    System.out.println("Fechando modulo de Compras");
                    leitor.nextLine();
                    break;
                default:
                    System.out.println("Opção invalida");
                    leitor.nextLine();
                    break;
            }
            this.util.limparTerminal();
        } while (opcao != 6);
    }

    private void listarProdutos() {
        System.out.println("\nTodos os produtos da lista");
        for (int indice = 0; indice < this.getQuantidadeItens(); indice++) {
            Produto produto = itens.get(indice);
            System.out.println(indice + " - " + produto.getNome() + " " + produto.getMarca() + " / Quantidade: "
                    + produto.getQuantidade() + " / Valor Unitario: " + produto.getPreco() + " / Valor Total: "
                    + (produto.getQuantidade() * produto.getPreco()));
        }
    }

}
