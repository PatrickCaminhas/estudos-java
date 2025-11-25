package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.nio.file.*;

public class ImpostoDeRenda {
    double rendimento;

    double DEDUCAO_DEPENDENTE = 189.59;
    double DEDUCAO_SIMPLIFICADA = 528.00;
    int dependente;
    Util util;

    public ImpostoDeRenda(double rendimento, int dependente) {
        this.rendimento = rendimento;
        this.dependente = dependente;
        this.util = new Util();
    }

    public ImpostoDeRenda() {
        this.util = new Util();

    }

    private int GetFaixaDeRendimento(double valor) {
        if (valor <= 2259.20) {
            return 1;
        } else if (valor > 2259.20 && valor <= 2826.65) {
            return 2;
        } else if (valor > 2826.65 && valor <= 3751.05) {
            return 3;
        } else if (valor > 3751.05 && valor <= 4664.68) {
            return 4;
        } else {
            return 5;
        }
    }

    private double GetAliquota(double valor) {
        int faixa = GetFaixaDeRendimento(valor);
        switch (faixa) {
            case 1:
                return 0;
            case 2:
                return 0.075;
            case 3:
                return 0.15;
            case 4:
                return 0.225;
            case 5:
                return 0.275;
            default:
                return 0;
        }
    }

    private double GetDeducao(double valor) {
        int faixa = GetFaixaDeRendimento(valor);
        switch (faixa) {
            case 1:
                return 0;
            case 2:
                return 169.44;
            case 3:
                return 381.44;
            case 4:
                return 662.77;
            case 5:
                return 896;
            default:
                return 0;
        }
    }

    public double GetINSS(double rendimento) {
        double faixa_1 = 0, faixa_2 = 0, faixa_3 = 0, faixa_4 = 0, teto;
        if (rendimento >= 4190.83) {
            if (rendimento > 8157.41) {
                teto = 8157.41;
            } else {
                teto = rendimento;
            }
            faixa_4 = (int) (((teto - 4190.83) * 0.14) * 100) / 100.0;
        }
        if ((rendimento > 2793.88 && rendimento <= 4190.83) || (rendimento > 4190.83)) {
            if (rendimento > 4190.83) {
                teto = 4190.83;
            } else {
                teto = rendimento;
            }
            faixa_3 = (int) (((teto - 2793.88) * 0.12) * 100) / 100.0;
        }
        if ((rendimento > 1518.00 && rendimento <= 2793.88) || (rendimento > 2793.88)) {
            if (rendimento > 2793.88) {
                teto = 2793.88;
            } else {
                teto = rendimento;
            }
            faixa_2 = (int) (((teto - 1518.00) * 0.09) * 100) / 100.0;
        }
        faixa_1 = (int) ((1518.00 * 0.075) * 100) / 100.0;

        return faixa_1 + faixa_2 + faixa_3 + faixa_4;
    }

    private double GetDeducaoPorDepedente() {
        return this.DEDUCAO_DEPENDENTE * this.dependente;
    }

    public double calcularImpostoDeRenda() {
        if (GetFaixaDeRendimento(this.rendimento - DEDUCAO_SIMPLIFICADA - GetINSS(this.rendimento)) == 1) {
            return 0.00;
        } else {
            double valorParaCalculo = this.rendimento - GetINSS(this.rendimento) - GetDeducaoPorDepedente();
            double resultado = (valorParaCalculo * GetAliquota(valorParaCalculo)) - GetDeducao(valorParaCalculo);
            return (int) (resultado * 100) / 100.00;
        }
    }

    public double calcularDecimoTerceiro() {
        return 0;
    }

    private int verificarMesInicialNoDT(LocalDate data) {
        int totalDiasDoMes = data.lengthOfMonth();
        int diaDaData = data.getDayOfMonth();
        if ((totalDiasDoMes - diaDaData + 1) > (totalDiasDoMes / 2)) {
            return 1;
        } else {
            return 0;
        }
    }

    private int quantosMesesDoAnoPassaram(LocalDate data) {
        try {
            int mesInicial = verificarMesInicialNoDT(data);
            int mesesPassados = data.getMonthValue();
            return 12 - mesesPassados + mesInicial;
        } catch (DateTimeParseException e) {
            System.out.println("Data inserida incorreta.\nExemplo correto: 01/01/2000");
            return 0;
        }

    }

    private double[] decimoTerceiro(LocalDate data) {

        int meses = 0;
        if (data.getYear() == LocalDate.now().getYear()) {
            meses = quantosMesesDoAnoPassaram(data);
        } else {
            meses = 12;
        }
        double valor = (this.rendimento / 12) * meses;
        return new double[] { (valor - GetINSS(valor)), (valor / 2), (valor / 2) - GetINSS(valor) };

    }

    public void iniciarProcesso() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("Informe seu salario bruto mensal: ");
        String rendimentoTexto = leitor.nextLine();
        Double rendimento = Double.parseDouble(rendimentoTexto);
        System.out.print("Informe sua quantidade de dependes: ");
        String dependenteTexto = leitor.nextLine();
        int dependente = Integer.parseInt(dependenteTexto);
        System.out.println("Utiliza vale transporte?\nS - Sim\nN - Nao");
        String transporte = "";
        Double valeTransporte = 0.0;

        do {
            transporte = leitor.nextLine();
            if (transporte.equalsIgnoreCase("S")) {
                System.out.println("Qual valor da passagem?");
                Double valorPassagem = Double.parseDouble(leitor.nextLine());
                System.out.println("Quantas passagens por dia?");
                Integer quantidadePassagensDiarias = Integer.parseInt(leitor.nextLine());
                System.out.println("Qual Escala você trabalha?\n1 - 5x2\n2 - 6x1");
                String escala = "";
                do {
                    escala = leitor.nextLine();
                } while (!escala.equalsIgnoreCase("1") && !escala.equalsIgnoreCase("2"));
                if (escala.equalsIgnoreCase("1"))
                    escala = "5x2";
                else
                    escala = "6x1";
                valeTransporte = calculaValeTransporte(valorPassagem, quantidadePassagensDiarias, escala);

            }
        } while (!transporte.equalsIgnoreCase("S") && !transporte.equalsIgnoreCase("N"));

        System.out.print("Informe a data que começou a trabalhar: ");
        String dataInicio = leitor.nextLine();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataCorrigida = LocalDate.parse(dataInicio, formatador);
        gerenciarArquivo(rendimento, dependente, dataCorrigida, valeTransporte);
        System.out.println("Salario");
    }

    public Double calculaValeTransporte(Double valorPassagem, Integer quantidadePassagensDiarias, String escala) {
        LocalDate dataAtual = LocalDate.now();
        return (valorPassagem * quantidadePassagensDiarias)
                * this.util.diasUteisTrabalho(dataAtual.getYear(), dataAtual.getMonthValue(), escala);
    }

    public String[] lerInformacoes() {
        Path caminho = Paths.get("dados/informacoes-renda.txt");
        if (!Files.exists(caminho)) {
            System.out.println("Arquivo de dados inexistente!");
            return new String[0];
        }
        try {
            String dadosCSV = Files.readAllLines(caminho).get(1);
            String[] dadosArray = dadosCSV.split(";");
            return dadosArray;
        } catch (Exception e) {
            System.out.println("Erro na leitura do arquivo.");
            return new String[0];
        }
    }

    public void gerenciarArquivo(double salarioBruto, int dependentes, LocalDate dataInicio, Double valeTransporte) {
        Path caminho = Paths.get("dados/informacoes-renda.txt");
        String cabecalho = "Salario Bruto;Dependentes;Data de inicio;INSS;Imposto de Renda;Vale Transporte;Salario Liquido;Decimo Terceiro;Data Modificacao\n";
        ImpostoDeRenda imposto = new ImpostoDeRenda(salarioBruto, dependentes);
        if (valeTransporte > (salarioBruto * 0.06)) {
            valeTransporte = salarioBruto * 0.06;
        }
        this.util.limparTerminal();
        double inss = imposto.GetINSS(salarioBruto), ir = imposto.calcularImpostoDeRenda();
        cabecalho += salarioBruto + ";" + dependentes + ";" + dataInicio + ";" + imposto.GetINSS(salarioBruto) + ";"
                + imposto.calcularImpostoDeRenda() + ";" + valeTransporte +";" + (salarioBruto - inss - ir - valeTransporte) + ";"
                + imposto.decimoTerceiro(dataInicio)[0] + ";"
                + LocalDate.now();
        try {
            Files.writeString(caminho, cabecalho);
            System.out.println("Arquivo 'informacoes-renda.txt' criado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
