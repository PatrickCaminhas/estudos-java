package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.YearMonth;
import java.nio.file.*;

public class Gastos {
    Double salarioLiquido;
    Double gastosRecorrentes;
    Double gastosEventuais;
    Double gastosTotais;
    Double resultado;
    Util util;

    public Gastos() {
        this.util = new Util();
    }

    public void setSalarioLiquido() {
        this.salarioLiquido = Double.parseDouble((String) lerInformacoes("dados/informacoes-renda.txt").get(6));
    }

    public void setGastosTotais() {
        this.gastosTotais = 0.0;
        ArrayList<String> listaPastas = this.util.pegarNomesSubPastas("dados/gastos/");
        ArrayList<String> listaArquivos = new ArrayList<>();
        for (String pasta : listaPastas) {
            LocalDate data = LocalDate.parse(pasta);
            LocalDate hoje = LocalDate.now();
            if (YearMonth.from(data).equals(YearMonth.from(hoje))) {
                listaArquivos.addAll(this.util.pegarNomesArquivos("dados/gastos/" + pasta + "/"));
            }
        }

        for (String caminhoArquivos : listaArquivos) {
            ArrayList<String> info = this.lerInformacoes(caminhoArquivos);
            this.gastosTotais += Double.parseDouble((String) info.get(3));
        }
    }

    public void setResultado() {
        if (this.salarioLiquido == null)
            setSalarioLiquido();
        if (this.gastosTotais == null)
            setGastosTotais();
        this.resultado = this.salarioLiquido - this.gastosTotais;
    }

    public ArrayList<String> lerInformacoes(String caminhoArquivo) {
        Path caminho = Paths.get(caminhoArquivo);
        if (!Files.exists(caminho)) {
            System.out.println("Arquivo de dados inexistente!");
            return new ArrayList<String>();
        }
        try {
            String dadosCSV = Files.readAllLines(caminho).get(1);
            String[] dadosArray = dadosCSV.split(";");
            ArrayList<String> dadosLista = new ArrayList<>(Arrays.asList(dadosArray));
            return dadosLista;
        } catch (Exception e) {
            System.out.println("Erro na leitura do arquivo.");
            return new ArrayList<String>();
        }
    }

}
