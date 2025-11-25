package modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Util {

    public void limparTerminal() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Não foi possível limpar o terminal.");
        }
    }

    public Integer diasUteisTrabalho(int ano, int mes, String escala) {
        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDate dataAtual = anoMes.atDay(1);
        LocalDate ultimoDia = anoMes.atEndOfMonth();
        int diasUteis = 0;

        while (!dataAtual.isAfter(ultimoDia)) {
            DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();
            if (escala.equalsIgnoreCase("5X2") || escala == null) {
                if (diaDaSemana != DayOfWeek.SATURDAY && diaDaSemana != DayOfWeek.SUNDAY) {
                    diasUteis++;
                }
            } else if (escala.equalsIgnoreCase("6X1")) {
                if (diaDaSemana != DayOfWeek.SUNDAY) {
                    diasUteis++;
                }
            }

            dataAtual = dataAtual.plusDays(1);

        }
        return diasUteis;
    }

    public ArrayList<String> pegarNomesArquivos(String caminhoDaPasta) {
        ArrayList<String> listaNomes = new ArrayList<>();
        Path pasta = Paths.get(caminhoDaPasta);

        try (Stream<Path> arquivos = Files.list(pasta)) {
            arquivos.forEach(arquivo -> {
                // Verifica se é arquivo (ignora subpastas)
                if (Files.isRegularFile(arquivo)) {

                    // A MÁGICA ACONTECE AQUI:
                    String apenasNome = arquivo.getParent().toString() + "\\"
                            + arquivo.getFileName().toString();

                    listaNomes.add(apenasNome);
                }
            });
        } catch (IOException e) {
            System.out.println("Erro ao ler a pasta: " + e.getMessage());
        }

        return listaNomes;
    }

    public ArrayList<String> pegarNomesSubPastas(String caminhoDaPastaPai) {
        ArrayList<String> listaPastas = new ArrayList<>();
        Path pastaPai = Paths.get(caminhoDaPastaPai);
        if (!Files.isDirectory(pastaPai)) {
            return new ArrayList<String>();
        }
        try (Stream<Path> itens = Files.list(pastaPai)) {
            itens.forEach(item -> {

                if (Files.isDirectory(item)) {

                    String nomePasta = item.getFileName().toString();
                    listaPastas.add(nomePasta);
                }
            });
        } catch (IOException e) {
            System.out.println("Erro ao ler: " + e.getMessage());
        }

        return listaPastas;
    }

    public String formatarDataISOparaBR(String data) {
        String[] arrayData = data.split("-");

        return arrayData[2] + "/" + arrayData[1] + "/" + arrayData[0];
    }

    public String formatarDataBRparaISO(String data) {
        String[] arrayData = data.split("/");
        return arrayData[2] + "-" + arrayData[1] + "-" + arrayData[0];
    }

    public String formatarNumeroParaCelular(String numero) {
        if (numero.length() != 11) {
            return "Erro";
        } else {
            return "(" + numero.substring(0, 2) + ") " + numero.substring(2, 8) + "-" + numero.substring(8);
        }
    }

    public String removerCaracteresEspeciais(String texto) {
        return texto.replaceAll("[^a-zA-Z0-9]", "");
    }

    public String converterDoubleParaReal(Double numero) {
        String valor = numero.toString();
        String[] arrayValor = valor.split(".");

        return "R$ " + arrayValor[0] + ","
                + ((arrayValor[1].length() > 1) ? arrayValor[1].substring(0, 2) : arrayValor[1] + "0");
    }

    public Double converterRealParaDouble(String real){
        String[] valor = real.split(",");
        return Double.parseDouble(removerCaracteresEspeciais(valor[0])+"."+valor[1]);
    }

    public String formatarNumeroParaCPF(String numero) {
        String primeiraParte = numero.substring(0, 3);
        String segundaParte = numero.substring(3, 6);
        String terceiraParte = numero.substring(6, 9);
        String digitoVerificador = numero.substring(9);

        return primeiraParte + "." + segundaParte + "." + terceiraParte + "-" + digitoVerificador;
    }

    

}
