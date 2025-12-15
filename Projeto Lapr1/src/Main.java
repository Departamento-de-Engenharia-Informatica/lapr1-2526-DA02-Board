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
                System.out.println("Erro: linhas têm número diferente de colunas no ficheiro " + filename);
                System.exit(0);
            }

            linhas++;
        }

        if (linhas != colunas) {
            System.out.println("Erro: matriz não é quadrada no ficheiro " + filename);
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

    public static int[][] somarMatrizes(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    public static void estabilizar(int[][] grid) {
        boolean instavel = true;

        while (instavel) {
            instavel = false;

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j] >= LIMITE) {
                        instavel = true;
                        topple(grid, i, j);
                    }
                }
            }
        }
    }

    public static void topple(int[][] grid, int i, int j) {
        grid[i][j] -= 4;

        if (i > 0) grid[i - 1][j]++; // logo acima
        if (i < grid.length - 1) grid[i + 1][j]++; // logo abaixo do valor
        if (j > 0) grid[i][j - 1]++; // á esquerda do valor
        if (j < grid.length - 1) grid[i][j + 1]++;// á direita do valor
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
    }
}
