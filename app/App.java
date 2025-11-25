package app;

import modelo.*;

import java.util.Scanner;

public class App {
    private Util util = new Util();
    private ImpostoDeRenda salario = new ImpostoDeRenda();
    private ComprasSupermercado supermercado;

    private void menu() {
        String menu = "";
        do {        Scanner leitor = new Scanner(System.in);

            this.util.limparTerminal();
            System.out.println("################[Aplicativo de Finanças]################");
            System.out.println(
                    "Selecione a opção\n1 - Calcular salario liquido e impostso\n2 - Cadastrar compras de Supermercado\n3 - Verificar saldo final do mês\n6- Sair");
            menu = leitor.nextLine();

            switch (menu) {
                case "1":
                    this.util.limparTerminal();
                    this.salario.iniciarProcesso();

                    break;
                case "2":
                    this.util.limparTerminal();

                    System.out.println("Digite o nome do supermercado");
                    this.supermercado = new ComprasSupermercado(leitor.nextLine());
                    this.supermercado.menu();
                    break;
                case "3":
                    this.util.limparTerminal();
                    System.out.println("Não implementado ainda");
                    break;
                case "6":
                    this.util.limparTerminal();
                    System.out.println("Finalização aplicativo...");
                    leitor.close();
                    break;

                default:
                    break;
            }
        } while (!menu.equals("6"));
        
    }

    public static void main(String[] args) {
        // ImpostoDeRenda Imposto = new ImpostoDeRenda();
        // Imposto.iniciarProcesso();
        // System.out.println(Imposto.calculaValeTransporte(5.74,2,"5x2"));
        // ComprasSupermercado listaCompras = new ComprasSupermercado("EPA");
        // listaCompras.menu();
        Gastos gastos = new Gastos();
        gastos.setResultado();
        // new App().menu();
    }
}