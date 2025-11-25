package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.nio.file.*;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class ImpostoDeRenda {
    BigDecimal rendimento;

    BigDecimal DEDUCAO_DEPENDENTE = BigDecimal.valueOf(189.59);
    BigDecimal DEDUCAO_SIMPLIFICADA = BigDecimal.valueOf(0528.00);
    int dependente;
    Util util;

    public ImpostoDeRenda(BigDecimal rendimento, int dependente) {
        this.rendimento = rendimento;
        this.dependente = dependente;
        this.util = new Util();
    }

    public ImpostoDeRenda() {
        this.util = new Util();

    }

    private int GetFaixaDeRendimento(BigDecimal valor) {
        if (valor.compareTo(new BigDecimal(2259.20)) <= 0) {
            return 1;
        } else if (valor.compareTo(new BigDecimal(2259.20)) > 0 && valor.compareTo(new BigDecimal(2826.65)) <= 0) {
            return 2;
        } else if (valor.compareTo(new BigDecimal(2826.65)) > 0 && valor.compareTo(new BigDecimal(3751.05)) <= 0) {
            return 3;
        } else if (valor.compareTo(new BigDecimal(3751.05)) > 0 && valor.compareTo(new BigDecimal(4664.68)) <= 0) {
            return 4;
        } else {
            return 5;
        }
    }

    private BigDecimal GetAliquota(BigDecimal valor) {
        int faixa = GetFaixaDeRendimento(valor);
        switch (faixa) {
            case 1:
                return new BigDecimal(0);
            case 2:
                return new BigDecimal(0.075);
            case 3:
                return new BigDecimal(0.15);
            case 4:
                return new BigDecimal(0.225);
            case 5:
                return new BigDecimal(0.275);
            default:
                return new BigDecimal(0);
        }
    }

    private BigDecimal GetDeducao(BigDecimal valor) {
        int faixa = GetFaixaDeRendimento(valor);
        switch (faixa) {
            case 1:
                return new BigDecimal(0);
            case 2:
                return new BigDecimal(169.44);
            case 3:
                return new BigDecimal(381.44);
            case 4:
                return new BigDecimal(662.77);
            case 5:
                return new BigDecimal(896);
            default:
                return new BigDecimal(0);
        }
    }

    public BigDecimal GetINSS(BigDecimal rendimento) {
        BigDecimal faixa_1 = new BigDecimal(0);
        BigDecimal faixa_2 = new BigDecimal(0);
        BigDecimal faixa_3 = new BigDecimal(0);
        BigDecimal faixa_4 = new BigDecimal(0);
        BigDecimal teto;

        BigDecimal valor_faixa_1 = new BigDecimal(1518.00);
        BigDecimal valor_faixa_2 = new BigDecimal(2793.88);
        BigDecimal valor_faixa_3 = new BigDecimal(4190.83);
        BigDecimal valor_faixa_4 = new BigDecimal(8157.41);

        if (rendimento.compareTo(valor_faixa_3) >= 0) {
            if (rendimento.compareTo(valor_faixa_4) > 0) {
                teto = valor_faixa_4;
            } else {
                teto = rendimento;
            }
            faixa_4 = (((teto.subtract(valor_faixa_3)).multiply(new BigDecimal(0.14))).multiply(new BigDecimal(100)))
                    .divide(new BigDecimal(100.0));
        }
        if ((rendimento.compareTo(valor_faixa_2) > 0 && rendimento.compareTo(valor_faixa_3) <= 0)
                || (rendimento.compareTo(valor_faixa_3) > 0)) {
            if (rendimento.compareTo(valor_faixa_3) > 0) {
                teto = valor_faixa_3;
            } else {
                teto = rendimento;
            }
            faixa_3 = (((teto.subtract(valor_faixa_2)).multiply(new BigDecimal(0.12))).multiply(new BigDecimal(100)))
                    .divide(new BigDecimal(100.0));
        }
        if ((rendimento.compareTo(valor_faixa_1) > 0 && rendimento.compareTo(valor_faixa_2) <= 0)
                || (rendimento.compareTo(valor_faixa_2) > 0)) {
            if (rendimento.compareTo(valor_faixa_2) > 0) {
                teto = valor_faixa_2;
            } else {
                teto = rendimento;
            }
            faixa_2 = (((teto.subtract(valor_faixa_1)).multiply(new BigDecimal(0.09))).multiply(new BigDecimal(100)))
                    .divide(new BigDecimal(100.0));
        }
        faixa_1 = (valor_faixa_1.multiply( new BigDecimal(0.075)).multiply( new BigDecimal(100))).divide( new BigDecimal(100.0));

        return faixa_1.add(faixa_2).add(faixa_3).add(faixa_4);
    }

    private BigDecimal GetDeducaoPorDepedente() {
        return this.DEDUCAO_DEPENDENTE.multiply( new BigDecimal(this.dependente));
    }

    public BigDecimal calcularImpostoDeRenda() {
        if (GetFaixaDeRendimento(this.rendimento.subtract(DEDUCAO_SIMPLIFICADA).subtract(GetINSS(this.rendimento))) == 1) {
            return new BigDecimal(0.00);
        } else {
            BigDecimal valorParaCalculo = this.rendimento.subtract( GetINSS(this.rendimento)).subtract( GetDeducaoPorDepedente());
            BigDecimal resultado = (valorParaCalculo.multiply( GetAliquota(valorParaCalculo))).subtract( GetDeducao(valorParaCalculo));
            return resultado.multiply( new BigDecimal(100.00)).divide( new BigDecimal(100.00));
        }
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

    private BigDecimal[] decimoTerceiro(LocalDate data) {

        int meses = 0;
        if (data.getYear() == LocalDate.now().getYear()) {
            meses = quantosMesesDoAnoPassaram(data);
        } else {
            meses = 12;
        }
        BigDecimal valor = (this.rendimento.divide( new BigDecimal(12))).multiply( new BigDecimal(meses));
        return new BigDecimal[] { (valor.subtract(GetINSS(valor))), (valor.divide( new BigDecimal(2))), (valor.divide( new BigDecimal(2))).subtract( GetINSS(valor)) };

    }

    public void iniciarProcesso() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("Informe seu salario bruto mensal: ");
        String rendimentoTexto = leitor.nextLine();
        BigDecimal rendimento = new BigDecimal(rendimentoTexto);
        System.out.print("Informe sua quantidade de dependes: ");
        String dependenteTexto = leitor.nextLine();
        int dependente = Integer.parseInt(dependenteTexto);
        System.out.println("Utiliza vale transporte?\nS - Sim\nN - Nao");
        String transporte = "";
        BigDecimal valeTransporte = new BigDecimal( 0.0);

        do {
            transporte = leitor.nextLine();
            if (transporte.equalsIgnoreCase("S")) {
                System.out.println("Qual valor da passagem?");
                BigDecimal valorPassagem = new BigDecimal(leitor.nextLine());
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

    public BigDecimal calculaValeTransporte(BigDecimal valorPassagem, Integer quantidadePassagensDiarias,
            String escala) {
        LocalDate dataAtual = LocalDate.now();
        return (valorPassagem.multiply(new BigDecimal(quantidadePassagensDiarias))).multiply(new BigDecimal(this.util.diasUteisTrabalho(dataAtual.getYear(), dataAtual.getMonthValue(), escala)));
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

    public void gerenciarArquivo(BigDecimal salarioBruto, int dependentes, LocalDate dataInicio,
            BigDecimal valeTransporte) {
        Path caminho = Paths.get("dados/informacoes-renda.txt");
        String cabecalho = "Salario Bruto;Dependentes;Data de inicio;INSS;Imposto de Renda;Vale Transporte;Salario Liquido;Decimo Terceiro;Data Modificacao\n";
        ImpostoDeRenda imposto = new ImpostoDeRenda(salarioBruto, dependentes);
        if (valeTransporte.compareTo( (salarioBruto.multiply( new BigDecimal(0.06))))>0) {
            valeTransporte = salarioBruto.multiply( new BigDecimal(0.06));
        }
        this.util.limparTerminal();
        BigDecimal inss = imposto.GetINSS(salarioBruto), ir = imposto.calcularImpostoDeRenda();
        cabecalho += salarioBruto + ";" + dependentes + ";" + dataInicio + ";" + imposto.GetINSS(salarioBruto) + ";"
                + imposto.calcularImpostoDeRenda() + ";" + valeTransporte + ";"
                + salarioBruto.subtract(inss).subtract(ir).subtract( valeTransporte) + ";"
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
