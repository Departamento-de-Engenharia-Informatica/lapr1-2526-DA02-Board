import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Main {

    static final int VALOR_CRITICO = 4;
    static final File MATRIZ_FINAL_OUTPUT = new File("output/matrizFinal.txt");


    public static int[][] laplacianaReduzida(int ordem) {

        int totalCelulas = ordem * ordem;
        int[][] matrizLaplaciana = new int[totalCelulas][totalCelulas];

        for (int linha = 0; linha < ordem; linha++) {
            for (int coluna = 0; coluna < ordem; coluna++) {

                int celula = linha * ordem + coluna;
                matrizLaplaciana[celula][celula] = 4;

                if (linha > 0) {
                    int vizinhoCima = (linha - 1) * ordem + coluna;
                    matrizLaplaciana[celula][vizinhoCima] = -1;
                }

                if (linha < ordem - 1) {
                    int vizinhoBaixo = (linha + 1) * ordem + coluna;
                    matrizLaplaciana[celula][vizinhoBaixo] = -1;
                }

                if (coluna > 0) {
                    int vizinhoEsquerda = linha * ordem + (coluna - 1);
                    matrizLaplaciana[celula][vizinhoEsquerda] = -1;
                }

                if (coluna < ordem - 1) {
                    int vizinhoDireita = linha * ordem + (coluna + 1);
                    matrizLaplaciana[celula][vizinhoDireita] = -1;
                }
            }
        }

        return matrizLaplaciana;
    }



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

    public static void escreverFicheiro(int[][] matriz) throws FileNotFoundException {

        PrintWriter writer = new PrintWriter(MATRIZ_FINAL_OUTPUT);

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {

                writer.print(matriz[i][j]);

                if (j < matriz[i].length - 1) {
                    writer.print(",");
                }
            }
            writer.println();
        }

        writer.close();
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

    private static void escreverImagem(int[][] matriz, int indice,
                                       HeatmapImageWriter imageWriter) {
        String outputPath = "output/output_image_" + indice + ".jpg";
        try {
            imageWriter.writeArrayAsImage(matriz, outputPath);
            System.out.println("Imagem escrita: " + outputPath);
        } catch (Exception e) {
            System.err.println("Erro ao escrever imagem: " + e.getMessage());
        }
    }

    private static void copiarPara3D(int[][] origem, int[][] destino) {
        for (int i = 0; i < origem.length; i++) {
            for (int j = 0; j < origem[i].length; j++) {
                destino[i][j] = origem[i][j];
            }
        }
    }

    public static int[][] estabilizar(int[][] matrizInicial) {

        HeatmapImageWriter imageWriter = new HeatmapImageWriter();

        final int MAX_MATRIZES = 99999;

        int linhas = matrizInicial.length;
        int colunas = matrizInicial[0].length;

        int[][][] historico = new int[MAX_MATRIZES][linhas][colunas];
        int contadorMatrizes = 0;

        int[][] matriz = new int[linhas][colunas];
        copiarPara3D(matrizInicial, matriz);

        copiarPara3D(matriz, historico[contadorMatrizes]);
        contadorMatrizes++;

        boolean instavel = true;

        while (instavel && contadorMatrizes < MAX_MATRIZES) {
            instavel = false;

            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    if (matriz[i][j] >= VALOR_CRITICO) {
                        instavel = true;
                        topple(matriz, i, j);
                    }
                }
            }

            if (instavel) {
                copiarPara3D(matriz, historico[contadorMatrizes]);
                contadorMatrizes++;
            }
        }

        if (contadorMatrizes <= 20) {

            for (int i = 0; i < contadorMatrizes; i++) {
                escreverImagem(historico[i], i, imageWriter);
            }

        } else {

            double passo = (double) (contadorMatrizes - 1) / 19;

            for (int i = 0; i < 20; i++) {
                int indice = (int) Math.round(i * passo);
                escreverImagem(historico[indice], i, imageWriter);
            }
        }

        return matriz;
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

        int[][] A = lerMatriz("input/matrizC");
        int[][] B = lerMatriz("input/matrizD");

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

        int[][] matrizEstabilizada = estabilizar(soma);

        System.out.println("Estabilizada:");
        imprimir(matrizEstabilizada);

        escreverFicheiro(matrizEstabilizada);

        boolean eRecorrente = burningDhar(matrizEstabilizada);
        if (eRecorrente) {
            System.out.println("A matriz é recorrente");
        } else {
            System.out.println("A matriz não e recorrente");
        }
        int[][] matrizLaplaciana = laplacianaReduzida(matrizEstabilizada.length);


    }
}
