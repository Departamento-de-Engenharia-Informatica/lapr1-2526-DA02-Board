import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    static final int LIMITE = 4;

    public static int[][] lerMatriz(String filename) throws FileNotFoundException {

        // --------------------------
        // Validar a matriz do ficheiro
        // --------------------------

        Scanner scanner = new Scanner(new File(filename));

        int linhas = 0;
        int colunas = -1;

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine().trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(",");
            if (colunas == -1) {
                colunas = partes.length;
            } else if (colunas != partes.length) {
                System.out.println("linhas tem número diferente de colunas no ficheiro " + filename);
                System.exit(0);
            }

            linhas++;
        }

        if (linhas != colunas) {
            System.out.println("matriz não e quadrada no ficheiro " + filename);
            System.exit(0);
        }

        scanner.close();

        int[][] matriz = new int[linhas][colunas];
        scanner = new Scanner(new File(filename));

        // --------------------------
        // Construir a matriz do ficheiro
        // --------------------------

        int i = 0;
        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine().trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(",");
            for (int j = 0; j < colunas; j++) {
                matriz[i][j] = Integer.parseInt(partes[j]);
            }
            i++;
        }

        scanner.close();
        return matriz;
    }

    public static boolean burningDhar(int[][] matriz) {
        int ordem = matriz.length;
        boolean[][] matrizQueimada = new boolean[ordem][ordem];
        boolean mudou;

        do {
            mudou = false;

            for (int i = 0; i < ordem; i++) {
                for (int j = 0; j < ordem; j++) {

                    int nVizinhos = 0;

                    if (!matrizQueimada[i][j]) {
                        if (i > 0 && !matrizQueimada[i - 1][j]) nVizinhos++; //cima
                        if (i < ordem - 1 && !matrizQueimada[i + 1][j]) nVizinhos++; // baixo
                        if (j > 0 && !matrizQueimada[i][j - 1]) nVizinhos++; // esquerda
                        if (j < ordem - 1 && !matrizQueimada[i][j + 1]) nVizinhos++; //direita

                        if (matriz[i][j] >= nVizinhos) {
                            matrizQueimada[i][j] = true;
                            mudou = true;
                        }
                    }
                }
            }
        } while (mudou);

        for (int i = 0; i < ordem; i++) {
            for (int j = 0; j < ordem; j++) {
                if (!matrizQueimada[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[][] somarMatrizes(int[][] matrizA, int[][] matrizB) {
        int ordem = matrizA.length;
        int[][] matrizSoma = new int[ordem][ordem];

        for (int i = 0; i < ordem; i++) {
            for (int j = 0; j < ordem; j++) {
                matrizSoma[i][j] = matrizA[i][j] + matrizB[i][j];
            }
        }
        return matrizSoma;
    }

    public static void estabilizar(int[][] matriz) {
        boolean instavel = true;

        while (instavel) {
            instavel = false;

            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz.length; j++) {
                    if (matriz[i][j] >= LIMITE) {
                        instavel = true;
                        topple(matriz, i, j);
                    }
                }
            }


            // guardar na imagem
        }
    }

    public static void topple(int[][] grid, int i, int j) {
        grid[i][j] -= 4;

        if (i > 0) grid[i - 1][j]++; // logo acima
        if (i < grid.length - 1) grid[i + 1][j]++; // logo abaixo do valor
        if (j > 0) grid[i][j - 1]++; // a esquerda do valor
        if (j < grid.length - 1) grid[i][j + 1]++;// a direita do valor
    }


    public static void imprimir(int[][] matriz) {
        for (int[] linha : matriz) {
            for (int val : linha) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {

        int[][] A = lerMatriz("matrizA");
        int[][] B = lerMatriz("matrizB");

        if (A.length != B.length) {
            System.out.println("Erro: matrizes têm ordens diferentes.");
            return;
        }

        System.out.println("Matriz A:");
        imprimir(A);

        System.out.println("Matriz B:");
        imprimir(B);

        int[][] soma = somarMatrizes(A, B);

        System.out.println("Soma:");
        imprimir(soma);

        estabilizar(soma);

        System.out.println("Estabilizada:");
        imprimir(soma);

        boolean eRecorrente = burningDhar(soma);
        if (eRecorrente) {
            System.out.println("A matriz é recorrente");
        } else {
            System.out.println("A matriz não e recorrente");
        }
    }
}
