import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

import org.apache.commons.math4.legacy.linear.*;

public class Main {

    static int TOTAL_MATRIZES = 0;
    static final int VALOR_CRITICO = 4;
    static boolean MODO_INTERATIVO = false;
    static PrintWriter OUT_WRITER = null;


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

    public static boolean verificarSeEstavel(int[][] matriz) {
        for (int linha=0; linha < matriz.length; linha++) {
            for (int coluna=0; coluna < matriz.length; coluna++) {
                if (matriz[linha][coluna] >= 4) {
                    return false;
                }
            }
        }
        return true;
    }


    public static int[][] lerMatriz(String filename, boolean verificarEstavel) throws FileNotFoundException {
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
                if (MODO_INTERATIVO) System.out.println("Erro: linhas com número diferente de colunas no ficheiro " + filename);
                if (!MODO_INTERATIVO) OUT_WRITER.println("Erro: linhas com número diferente de colunas no ficheiro " + filename);
                System.exit(2);
            }

            linhas++;
        }

        if (linhas != colunas) {
            if (MODO_INTERATIVO) System.out.println("Erro: matriz não é quadrada no ficheiro " + filename);
            if (!MODO_INTERATIVO) OUT_WRITER.println("Erro: matriz não é quadrada no ficheiro " + filename);
            System.exit(2);
        }

        scanner.close();

        int[][] matriz = new int[linhas][colunas];
        scanner = new Scanner(new File(filename));

        int i = 0;
        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine().trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(",");

            for (int j = 0; j < colunas; j++) {
                try {
                    int valor = Integer.parseInt(partes[j].trim());

                    if (verificarEstavel && valor < 3) {
                        if (MODO_INTERATIVO) System.out.println("Matriz não é estável! | Ficheiro: " + filename);
                        if (!MODO_INTERATIVO) OUT_WRITER.println("Matriz não é estável! | Ficheiro: " + filename);
                        System.exit(2);
                    }

                    matriz[i][j] = valor;

                } catch (NumberFormatException e) {
                    if (MODO_INTERATIVO) System.out.println(
                            "Erro: valor não numérico na linha " + (i + 1) +
                                    ", coluna " + (j + 1) +
                                    " | Ficheiro: " + filename
                    );
                    if (!MODO_INTERATIVO) OUT_WRITER.println(
                            "Erro: valor não numérico na linha " + (i + 1) +
                                    ", coluna " + (j + 1) +
                                    " | Ficheiro: " + filename
                    );
                    System.exit(3);
                }
            }
            i++;
        }

        return matriz;
    }


    public static void escreverFicheiro(int[][] matriz) throws FileNotFoundException {

        Scanner scanner = new Scanner(System.in);
        File outFile = new File(scanner.nextLine());
        PrintWriter writer = new PrintWriter(outFile);

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
            if (MODO_INTERATIVO) System.out.println("Imagem escrita: " + outputPath);
            if (!MODO_INTERATIVO) OUT_WRITER.println("Imagem escrita: " + outputPath);
        } catch (Exception e) {
            if (MODO_INTERATIVO) System.out.println("Erro ao escrever imagem: " + e.getMessage());
            if (!MODO_INTERATIVO) OUT_WRITER.println("Erro ao escrever imagem: " + e.getMessage());
        }
    }

    private static void copiarPara(int[][] origem, int[][] destino) {
        for (int i = 0; i < origem.length; i++) {
            for (int j = 0; j < origem[i].length; j++) {
                destino[i][j] = origem[i][j];
            }
        }
    }

    private static void estabilizarParticular(int [][] matriz) {
        boolean instavel = true;

        int ordem = matriz.length;

        while (instavel) {
            instavel = false;

            for (int i = 0; i < ordem; i++) {
                for (int j = 0; j < ordem; j++) {
                    while (matriz[i][j] >= VALOR_CRITICO) {
                        instavel = true;
                        topple(matriz, i, j);
                    }
                }
            }
        }
    }

    public static void continuacaoEx3(int[][] matrizInicial) {

        HeatmapImageWriter imageWriter = new HeatmapImageWriter();

        final int MAX_MATRIZES = 16384;

        int linhas = matrizInicial.length;
        int colunas = matrizInicial[0].length;

        int[][][] historico = new int[MAX_MATRIZES][linhas][colunas];
        int contadorMatrizes = 0;

        int[][] matriz = new int[linhas][colunas];
        copiarPara(matrizInicial, matriz);

        copiarPara(matriz, historico[contadorMatrizes]);
        contadorMatrizes++;

        boolean instavel = true;

        while (instavel && contadorMatrizes < MAX_MATRIZES) {
            instavel = false;

            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    while (matriz[i][j] >= VALOR_CRITICO) {
                        instavel = true;
                        topple(matriz, i, j);
                    }
                }
            }

            if (instavel) {
                copiarPara(matriz, historico[contadorMatrizes]);
                contadorMatrizes++;
            }
        }

        if (MODO_INTERATIVO) System.out.println("Percorremos a matriz " + contadorMatrizes + " vezes para a estabilizar!");
        if (!MODO_INTERATIVO) OUT_WRITER.println("Percorremos a matriz " + contadorMatrizes + " vezes para a estabilizar!");
        if (contadorMatrizes <= 20) {

            for (int i = 0; i < contadorMatrizes; i++) {
                escreverImagem(historico[i], i, imageWriter);
            }
            if (MODO_INTERATIVO) System.out.println();
            if (!MODO_INTERATIVO) OUT_WRITER.println();

        } else {

            double passo = (double) (contadorMatrizes - 1) / 19;
            for (int i = 0; i < 20; i++) {
                int indice = (int) Math.round(i * passo);
                escreverImagem(historico[indice], i, imageWriter);
            }
        }
    }


    public static void topple(int[][] grid, int i, int j) {
        grid[i][j] -= 4;

        if (i > 0) grid[i - 1][j]++;
        if (i < grid.length - 1) grid[i + 1][j]++;
        if (j > 0) grid[i][j - 1]++;
        if (j < grid[i].length - 1) grid[i][j + 1]++;
    }


    public static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (j == matriz.length - 1) {
                    if (MODO_INTERATIVO) System.out.print(matriz[i][j]);
                } else {
                    if (MODO_INTERATIVO) System.out.print(matriz[i][j] + ",");
                }
            }
            if (MODO_INTERATIVO) System.out.println();
        }
        if (MODO_INTERATIVO) System.out.println();
    }

    public static void imprimirVetorProprio(double[][] matriz) {
        String bob = "[";
        boolean first = true;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {

                if (!first) {
                    bob += ",";
                }

                bob += matriz[i][j];
                first = false;
            }
        }

        bob += "]";

        if (MODO_INTERATIVO) System.out.println(bob);
        if (!MODO_INTERATIVO) OUT_WRITER.println(bob);
    }



    public static double determinanteLaplaciana(int[][] laplaciana) {

        int n = laplaciana.length;
        double[][] matrizDouble = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizDouble[i][j] = laplaciana[i][j];
            }
        }

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrizDouble);
        LUDecomposition lu = new LUDecomposition(matrix);

        return lu.getDeterminant();
    }

    public static double[][] espectroLaplaciana(int[][] laplaciana) {

        int ordem = laplaciana.length;
        double[][] matrizDouble = new double[ordem][ordem];

        for (int i = 0; i < ordem; i++) {
            for (int j = 0; j < ordem; j++) {
                matrizDouble[i][j] = laplaciana[i][j];
            }
        }

        RealMatrix matriz = MatrixUtils.createRealMatrix(matrizDouble);
        EigenDecomposition decomposicao = new EigenDecomposition(matriz);

        double[][] resultados = new double[ordem + 1][ordem]; // o ordem + 1 é para ser uma matriz de resultados e possiblitar guardar os valores proprios na primeira linha da matriz, sendo as outras linhas preenchidas pelos vetores proprios de cada linha da matriz laplaciana

        for (int k = 0; k < ordem; k++) {
            double valorProprio = decomposicao.getRealEigenvalue(k);
            resultados[0][k] = Math.round(valorProprio * 1000.0) / 1000.0;
        }

        for (int k = 0; k < ordem; k++) {
            RealVector vetorProprio = decomposicao.getEigenvector(k);

            double maxAbsoluto = 0.0;
            for (int i = 0; i < ordem; i++) {
                maxAbsoluto = Math.max(maxAbsoluto, Math.abs(vetorProprio.getEntry(i)));
            }

            for (int i = 0; i < ordem; i++) {
                double valor = vetorProprio.getEntry(i);

                if (maxAbsoluto != 0) {
                    valor /= maxAbsoluto;
                }
                resultados[k + 1][i] = (Math.round(valor * 1000.0) / 1000.0) * -1;
            }
        }

        return resultados;
    }


    public static void imprimirEspectro(double[][] resultados) {

        int ordem = resultados[0].length;

        String valores = "[";

        for (int i = 0; i < ordem; i++) {
            if (i == ordem - 1) {
                valores += resultados[0][i];
            } else {
                valores += resultados[0][i] + ",";
            }
        }

        valores += "]";

        if (MODO_INTERATIVO) System.out.println("Valores Próprios:");
        if (MODO_INTERATIVO) System.out.println(valores);
        if (!MODO_INTERATIVO) OUT_WRITER.println("Valores Próprios:");
        if (!MODO_INTERATIVO) OUT_WRITER.println(valores);

        if (MODO_INTERATIVO) System.out.println("Vetores Próprios:");
        if (!MODO_INTERATIVO) OUT_WRITER.println("Vetores Próprios:");


        for (int i = 1; i <= ordem; i++) {
            String vetor = "[";

            for (int j = 0; j < ordem; j++) {
                if (j == ordem - 1) {
                    vetor += resultados[i][j];
                } else {
                    vetor += resultados[i][j] + ",";
                }
            }

            vetor += "]";

            if (MODO_INTERATIVO) System.out.println(vetor);
            if (!MODO_INTERATIVO) OUT_WRITER.println(vetor);
        }
    }



    public static void primeiroExercicio(Scanner scanner) throws FileNotFoundException {
        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);
        imprimirMatriz(matriz);

    }

    public static void segundoExercicio(Scanner scanner) throws FileNotFoundException {
        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);

        if (MODO_INTERATIVO) System.out.println("Matriz Carregada: ");
        imprimirMatriz(matriz);

        if (verificarSeEstavel(matriz)) {
            if (MODO_INTERATIVO) System.out.println("A matriz é estável");
        } else {
            if (MODO_INTERATIVO) System.out.println("A matriz não é estável");
            estabilizarParticular(matriz);

            if (MODO_INTERATIVO) System.out.println("Matriz estabilizada: ");
            imprimirMatriz(matriz);
            escreverFicheiro(matriz);
        }
    }

    public static void terceiroExercicio(Scanner scanner) throws FileNotFoundException {

        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro da configuração atual: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matrizA = lerMatriz("input/" + nomeFicheiro, false);

        if (verificarSeEstavel(matrizA)) {
            if (MODO_INTERATIVO) System.out.print("Nome do ficheiro das novas tarefas: ");
            nomeFicheiro = scanner.nextLine();
            if (MODO_INTERATIVO) System.out.println();
            int[][] matrizB = lerMatriz("input/" + nomeFicheiro, false);

            int[][] somaMatrizes = somarMatrizes(matrizA, matrizB);
            continuacaoEx3(somaMatrizes);

        } else {
            if (MODO_INTERATIVO) System.out.println("A Configuração Atual não é estavel!");
            if (!MODO_INTERATIVO) OUT_WRITER.println("A Configuração Atual não é estavél!");
        }
    }

    public static void quartoExercicio(Scanner scanner) throws FileNotFoundException {
        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);

        if (verificarSeEstavel(matriz)) {
            if (burningDhar(matriz)) {
                if (MODO_INTERATIVO) System.out.println("A matriz é recorrente");
            } else {
                if (MODO_INTERATIVO) System.out.println("A matriz não é recorrente");
            }
        } else {
            if (MODO_INTERATIVO) System.out.println("A matriz não é estável logo nao posso aplicar o algoritmo burning de dhar");
        }
    }

    public static boolean matrizesIguais(int[][] matrizA, int[][] matrizB) {
        for (int i = 0; i < matrizA.length; i++) {
            for (int j = 0; j < matrizA[i].length; j++) {
                if (matrizA[i][j] != matrizB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    private static boolean gerarEVerificar(int[][] atual, int pos, int[][] matrizE) {
        int ordem = atual.length;

        if (pos == ordem * ordem) {
            TOTAL_MATRIZES++;

            if (!burningDhar(atual)) {
                return true;
            }

            int[][] soma = somarMatrizes(atual, matrizE);
            estabilizarParticular(soma);

            if (!matrizesIguais(atual, soma)) {
                return false;
            }
            return true;
        }

        int i = pos / ordem;
        int j = pos % ordem;

        for (int valor = 0; valor < 4; valor++) {
            atual[i][j] = valor;

            if (!gerarEVerificar(atual, pos + 1, matrizE)) {
                return false;
            }
        }

        return true;
    }



    public static boolean verificarElementoNeutro(int[][] matrizE, boolean imprimirTentativas) {
        int ordem = matrizE.length;
        int[][] atual = new int[ordem][ordem];

        TOTAL_MATRIZES = 0;

        boolean resultado = gerarEVerificar(atual, 0, matrizE);

        if (imprimirTentativas) if (MODO_INTERATIVO) System.out.println("Matrizes geradas e testadas: " + TOTAL_MATRIZES);
        if (imprimirTentativas) if (!MODO_INTERATIVO) OUT_WRITER.println("Matrizes geradas e testadas: " + TOTAL_MATRIZES);

        return resultado;
    }

    public static void quintoExercicio(Scanner scanner) throws FileNotFoundException {
        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro que contem a matriz E: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matrizE = lerMatriz("input/" + nomeFicheiro, false);

        if (!verificarSeEstavel(matrizE)) {
            if (MODO_INTERATIVO) System.out.println("E não é estável, logo não pode ser elemento neutro.");
            return;
        }

        if (!burningDhar(matrizE)) {
            if (MODO_INTERATIVO) System.out.println("E não é recorrente, logo não pertence a R");
            return;
        }

        boolean eNeutro = verificarElementoNeutro(matrizE, true);

        if (eNeutro) {
            if (MODO_INTERATIVO) System.out.println("A matriz E é elemento neutro da operação + em r");
        } else {
            if (MODO_INTERATIVO) System.out.println("A matriz E não é elemento neutro da operação ⊕ em R");
        }
    }

    private static int contarRecorrentesRec(int[][] atual, int pos) {
        int n = atual.length;

        if (pos == n * n) {
            if (burningDhar(atual)) {
                return 1;
            } else {
                return 0;
            }
        }

        int i = pos / n;
        int j = pos % n;

        int total = 0;

        for (int v = 0; v < 4; v++) {
            atual[i][j] = v;
            total += contarRecorrentesRec(atual, pos + 1);
        }

        return total;
    }


    public static int contarConfiguracoesRecorrentes(int ordem) {
        int[][] atual = new int[ordem][ordem];
        return contarRecorrentesRec(atual, 0);
    }


    public static void sextoExercicio(Scanner scanner) {
        if (MODO_INTERATIVO) System.out.print("Ordem da matriz: ");
        int ordem = Integer.parseInt(scanner.nextLine());
        long inicio = System.nanoTime();
        int total = contarConfiguracoesRecorrentes(ordem);
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        if (MODO_INTERATIVO) System.out.println("Número de configurações recorrentes: " + total);
        if (MODO_INTERATIVO) System.out.printf("(6) Tempo de execução: %.3f ms%n", tempoMs);
    }

    public static void setimoExercicio(Scanner scanner) {
        if (MODO_INTERATIVO) System.out.print("Ordem da matriz: ");
        int ordem = Integer.parseInt(scanner.nextLine());
        long inicio = System.nanoTime();
        int[][] laplaciana = laplacianaReduzida(ordem);
        int determinanteLaplaciana = (int) Math.round(determinanteLaplaciana(laplaciana));
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        if (MODO_INTERATIVO) System.out.println("Numero configurações recorrentes: " + determinanteLaplaciana);
        if (MODO_INTERATIVO) System.out.printf("(7) Tempo de execução: %.3f ms%n", tempoMs);
    }


    private static boolean gerarETestar(
            int[][] matrizTentativa,
            int pos,
            int[][] matrizA,
            int[][] matrizE,
            int[][] resultado
    ) {
        int n = matrizTentativa.length;

        if (pos == n * n) {

            TOTAL_MATRIZES++;

            int[][] soma = somarMatrizes(matrizA, matrizTentativa);
            estabilizarParticular(soma);

            if (matrizesIguais(soma, matrizE)) {
                copiarPara(matrizTentativa, resultado);
                return true;
            }

            return false;
        }

        int i = pos / n;
        int j = pos % n;

        for (int v = 0; v < 4; v++) {
            matrizTentativa[i][j] = v;

            if (gerarETestar(matrizTentativa, pos + 1, matrizA, matrizE, resultado)) {
                return true;
            }
        }

        return false;
    }


    public static int[][] encontrarInversa(int[][] matrizA, int[][] matrizE) {
        int ordem = matrizA.length;
        int[][] matrizTentativa = new int[ordem][ordem];
        int[][] resultado = new int[ordem][ordem];

        boolean encontrada = gerarETestar(matrizTentativa, 0, matrizA, matrizE, resultado);

        if (!encontrada) {
            if (MODO_INTERATIVO) System.out.println("Não foi encontrada nenhuma matriz inversa");
            if (!MODO_INTERATIVO) OUT_WRITER.println("Não foi encontrada nenhuma matriz inversa");
            return null;
        } else {
            if (MODO_INTERATIVO) System.out.println("Foi encontrada inversa em " + TOTAL_MATRIZES + " tentativas!");
            if (!MODO_INTERATIVO) OUT_WRITER.println("Foi encontrada inversa em " + TOTAL_MATRIZES + " tentativas!");
        }

        return resultado;
    }

    public static void oitavoExercicio(Scanner scanner) throws FileNotFoundException {

        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro da matriz A: ");
        String nomeA = scanner.nextLine();

        if (MODO_INTERATIVO) System.out.print("Nome do ficheiro da matriz E: ");
        String nomeE = scanner.nextLine();

        int[][] matrizA = lerMatriz("input/" + nomeA, false);
        int[][] matrizE = lerMatriz("input/" + nomeE, false);

        if (MODO_INTERATIVO) System.out.println();

        if (!verificarSeEstavel(matrizA)) {
            if (MODO_INTERATIVO) System.out.println("A não é estável, logo não pertence a R");
            return;
        }

        if (!burningDhar(matrizA)) {
            if (MODO_INTERATIVO) System.out.println("A não é recorrente, logo não tem inversa em R");
            return;
        }

        if (matrizA.length != matrizE.length) {
            if (MODO_INTERATIVO) System.out.println("As matrizes nao têm a mesma ordem");
            return;
        }

        if (!verificarSeEstavel(matrizE)) {
            if (MODO_INTERATIVO) System.out.println("E não é estável, logo não pode ser elemento neutro.");
            return;
        }

        if (!burningDhar(matrizE)) {
            if (MODO_INTERATIVO) System.out.println("E não é recorrente, logo não pertence a R");
            return;
        }

        if (!verificarElementoNeutro(matrizE, false)) {
            if (MODO_INTERATIVO) System.out.println("A matriz E não é elemento neutro da operação ⊕ em R");
            return;
        }

        TOTAL_MATRIZES = 0;

        int[][] inversa = encontrarInversa(matrizA, matrizE);
        if (inversa != null) {
            if (MODO_INTERATIVO) System.out.println("Matriz inversa de A relativamente á adicao estabilizada:");
            imprimirMatriz(inversa);
        }
    }

    private static void normalizar(double[][] vetorProprio) {
        double maxAbsoluto = 0.0;

        for (int i = 0; i < vetorProprio.length; i++) {
            for (int j = 0; j < vetorProprio[i].length; j++) {
                double x = vetorProprio[i][j];
                maxAbsoluto = Math.max(maxAbsoluto, Math.abs(x));
            }
        }

        if (maxAbsoluto == 0) return;

        for (int i = 0; i < vetorProprio.length; i++) {
            for (int j = 0; j < vetorProprio[i].length; j++) {
                vetorProprio[i][j] /= maxAbsoluto;
            }
        }
    }

    public static void imprimirValoresProprios(double[] arrValoresProprios) {
        String bob = "[";
        for (int i=0; i < arrValoresProprios.length; i++) {
            if (i == arrValoresProprios.length - 1) {
                bob += (arrValoresProprios[i] + "]");
            } else {
                bob += (arrValoresProprios[i] + ",");
            }
        }
        if (MODO_INTERATIVO){
            System.out.println("Valores Próprios: ");
            System.out.println(bob);
        }
        if (!MODO_INTERATIVO) {
            OUT_WRITER.print("Valores Próprios: ");
            OUT_WRITER.println(bob);
        }
    }


    public static void formulaFechada(int ordem) {
        double[] arrValoresProprios = new double[ordem * ordem];

        if (MODO_INTERATIVO) System.out.println("Vetores Próprios: ");
        if (!MODO_INTERATIVO) OUT_WRITER.println("Vetores Próprios: ");
        for (int k = 1; k <= ordem; k++) {
            for (int l = 1; l <= ordem; l++) {

                double valorProprio = 4 - 2 * Math.cos(k * Math.PI / (ordem + 1)) - 2 * Math.cos(l * Math.PI / (ordem + 1));
                int indice = (k-1) * ordem + (l - 1);
                valorProprio = Math.round(valorProprio * 1000.0) / 1000.0;
                arrValoresProprios[indice] = valorProprio;

                double[][] vetorProprio = new double[ordem][ordem];

                for (int i = 1; i <= ordem; i++) { // nao pode começar no 0 por causa das contas
                    for (int j = 1; j <= ordem; j++) { // nao pode começar no 0 por causa das contas

                        vetorProprio[i - 1][j - 1] = Math.round(Math.sin(k * i * Math.PI / (ordem + 1)) * Math.sin(l * j * Math.PI / (ordem + 1)) * 1000.0) / 1000.0;
                    }
                }
                normalizar(vetorProprio);

                imprimirVetorProprio(vetorProprio);
            }
        }
        if (MODO_INTERATIVO) System.out.println();
        if (!MODO_INTERATIVO) OUT_WRITER.println();
        imprimirValoresProprios(arrValoresProprios);
    }


    public static void decimoExercicio(Scanner scanner) {
        if (MODO_INTERATIVO) System.out.print("Ordem da matriz: ");
        int ordem = Integer.parseInt(scanner.nextLine());
        long inicio = System.nanoTime();
        formulaFechada(ordem);
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        if (MODO_INTERATIVO) System.out.printf("(10) Tempo de execução: %.3f ms%n", tempoMs);
    }

    public static void nonoExercicio(Scanner scanner) {
        if (MODO_INTERATIVO) System.out.print("Ordem da matriz: ");
        int ordem = Integer.parseInt(scanner.nextLine());

        long inicio = System.nanoTime();
        int[][] laplaciana = laplacianaReduzida(ordem);
        double[][] matrizResultados = espectroLaplaciana(laplaciana);
        imprimirEspectro(matrizResultados);
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        if (MODO_INTERATIVO) System.out.printf("(9) Tempo de execução: %.3f ms%n", tempoMs);
    }


    public static void imprimirMenu() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        if (MODO_INTERATIVO) System.out.println();

        int escolha = -1;

        while (escolha != 0) {
            for (int i=0; i<70; i++) if (MODO_INTERATIVO) System.out.print("-");
            if (MODO_INTERATIVO) System.out.println();
            if (MODO_INTERATIVO) System.out.println("[1] Imprimir Matriz do Ficheiro");
            if (MODO_INTERATIVO) System.out.println("[2] Verificar se a matriz é estável");
            if (MODO_INTERATIVO) System.out.println("[3] Adicionar tarefas á configuração atual");
            if (MODO_INTERATIVO) System.out.println("[4] Verificar se a matriz é recorrente");
            if (MODO_INTERATIVO) System.out.println("[5] Verificar elemento neutro");
            if (MODO_INTERATIVO) System.out.println("[6] Calcular número de configurações recorrentes sem Laplaciana");
            if (MODO_INTERATIVO) System.out.println("[7] Calcular número de configurações recorrentes com Laplaciana");
            if (MODO_INTERATIVO) System.out.println("[8] Calcular a matriz inversa relativamente à adição estabilizada");
            if (MODO_INTERATIVO) System.out.println("[9] Calcular valores e vetores próprios sem a formula fechada");
            if (MODO_INTERATIVO) System.out.println("[10] Calcular valores e vetores próprios com a formula fechada");
            if (MODO_INTERATIVO) System.out.println();
            if (MODO_INTERATIVO) System.out.println("[0] Sair");

            if (MODO_INTERATIVO) System.out.print("--> ");
            escolha = Integer.parseInt(scanner.nextLine());
            if (MODO_INTERATIVO) System.out.println();

            switch (escolha) {
                case 1:
                    primeiroExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 2:
                    segundoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 3:
                    terceiroExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 4:
                    quartoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 5:
                    quintoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                case 6:
                    sextoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 7:
                    setimoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 8:
                    oitavoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 9:
                    nonoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 10:
                    decimoExercicio(scanner);
                    if (MODO_INTERATIVO) System.out.println();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    if (MODO_INTERATIVO) System.out.println("Opção Inválida!");
                    System.exit(1);
                    break;
            }
        }
    }

    public static void imprimirMatrizNI (int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (j == matriz.length - 1) {
                    OUT_WRITER.print(matriz[i][j]);
                } else {
                    OUT_WRITER.print(matriz[i][j] + ",");
                }
            }
            OUT_WRITER.println();
        }
        OUT_WRITER.println();
    }

    public static void primeiroExercicioNI(String nomeFicheiro) throws FileNotFoundException {
        int [][] matriz = lerMatriz("input/" + nomeFicheiro, false);
        imprimirMatrizNI(matriz);
    }

    public static void segundoExercicioNI(String nomeFicheiro) throws FileNotFoundException {
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);

        OUT_WRITER.println("Matriz Carregada:");

        imprimirMatrizNI(matriz);
        if (verificarSeEstavel(matriz)) {
            OUT_WRITER.println("A matriz é estável !");
        } else {
            OUT_WRITER.println("A matriz não é estável!");
            estabilizarParticular(matriz);

            OUT_WRITER.println("\nMatriz Estabilizada: ");
            imprimirMatrizNI(matriz);
        }
    }

    public static void terceiroExercicioNI(String nomeMatrizA, String nomeMatrizB) throws FileNotFoundException {

        int[][] matrizA = lerMatriz("input/" + nomeMatrizA, false);

        if (verificarSeEstavel(matrizA)) {
            int[][] matrizB = lerMatriz("input/" + nomeMatrizB, false);

            int[][] somaMatrizes = somarMatrizes(matrizA, matrizB);
            continuacaoEx3(somaMatrizes);

        } else {
            OUT_WRITER.println("A Configuração Atual não é estavél!");
        }
    }

    public static void quartoExercicioNI(String nomeFicheiro) throws FileNotFoundException {
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);

        if (verificarSeEstavel(matriz)) {
            if (burningDhar(matriz)) {
                OUT_WRITER.println("A matriz é recorrente");
            } else {
                OUT_WRITER.println("A matriz não é recorrente");
            }
        } else {
            OUT_WRITER.println("A matriz não é estável logo nao posso aplicar o algoritmo burning de dhar");
        }
    }

    public static void quintoExercicioNI(String nomeFicheiro) throws FileNotFoundException {
        int[][] matrizE = lerMatriz("input/" + nomeFicheiro, false);

        if (!verificarSeEstavel(matrizE)) {
            OUT_WRITER.println("E não é estável, logo não pode ser elemento neutro.");
            return;
        }

        if (!burningDhar(matrizE)) {
            OUT_WRITER.println("E não é recorrente, logo não pertence a R");
            return;
        }

        boolean eNeutro = verificarElementoNeutro(matrizE, true);

        if (eNeutro) {
            OUT_WRITER.println("A matriz E é elemento neutro da operação + em r");
        } else {
            OUT_WRITER.println("A matriz E não é elemento neutro da operação ⊕ em R");
        }
    }

    public static void sextoExercicioNI(int ordem) {
        long inicio = System.nanoTime();
        int total = contarConfiguracoesRecorrentes(ordem);
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        OUT_WRITER.println("Número de configurações recorrentes: " + total);
        OUT_WRITER.printf("(6) Tempo de execução: %.3f ms%n", tempoMs);
    }

    public static void setimoExercicioNI(int ordem) {
        long inicio = System.nanoTime();
        int[][] laplaciana = laplacianaReduzida(ordem);
        int determinanteLaplaciana = (int) Math.round(determinanteLaplaciana(laplaciana));
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        OUT_WRITER.println("Numero configurações recorrentes: " + determinanteLaplaciana);
        OUT_WRITER.printf("(7) Tempo de execução: %.3f ms%n", tempoMs);
    }

    public static void oitavoExercicioNI(String nomeFicheiroA, String nomeFicheiroE) throws FileNotFoundException {

        int[][] matrizA = lerMatriz("input/" + nomeFicheiroA, false);
        int[][] matrizE = lerMatriz("input/" + nomeFicheiroE, false);

        if (!verificarSeEstavel(matrizA)) {
            OUT_WRITER.println("A não é estável, logo não pertence a R");
            return;
        }

        if (!burningDhar(matrizA)) {
            OUT_WRITER.println("A não é recorrente, logo não tem inversa em R");
            return;
        }

        if (matrizA.length != matrizE.length) {
            OUT_WRITER.println("As matrizes nao têm a mesma ordem");
            return;
        }

        if (!verificarSeEstavel(matrizE)) {
            OUT_WRITER.println("E não é estável, logo não pode ser elemento neutro.");
            return;
        }

        if (!burningDhar(matrizE)) {
            OUT_WRITER.println("E não é recorrente, logo não pertence a R");
            return;
        }

        if (!verificarElementoNeutro(matrizE, false)) {
            OUT_WRITER.println("A matriz E não é elemento neutro da operação ⊕ em R");
            return;
        }

        TOTAL_MATRIZES = 0;

        int[][] inversa = encontrarInversa(matrizA, matrizE);
        if (inversa != null) {
            OUT_WRITER.println("Matriz inversa de A relativamente á adicao estabilizada:");
            imprimirMatrizNI(inversa);
        }
    }

    public static void nonoExercicioNI(int ordem) {
        long inicio = System.nanoTime();
        int[][] laplaciana = laplacianaReduzida(ordem);
        double[][] matrizResultados = espectroLaplaciana(laplaciana);
        imprimirEspectro(matrizResultados);
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        OUT_WRITER.printf("(9) Tempo de execução: %.3f ms%n", tempoMs);
    }

    public static void decimoExercicioNI(int ordem) {
        long inicio = System.nanoTime();
        formulaFechada(ordem);
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;
        OUT_WRITER.printf("(10) Tempo de execução: %.3f ms%n", tempoMs);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            MODO_INTERATIVO = true;
            imprimirMenu();
            return;
        } else {
            MODO_INTERATIVO = false;

            String f = null;
            String a = null;
            String b = null;
            String e = null;
            String o = null;
            String d = null;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-f":
                        if (i + 1 < args.length) f = args[++i];
                        break;
                    case "-a":
                        if (i + 1 < args.length) a = args[++i];
                        break;
                    case "-b":
                        if (i + 1 < args.length) b = args[++i];
                        break;
                    case "-e":
                        if (i + 1 < args.length) e = args[++i];
                        break;
                    case "-o":
                        if (i + 1 < args.length) o = "output/" + args[++i];
                        File outFile = new File(o);
                        OUT_WRITER = new PrintWriter(outFile);
                        break;
                    case "-d":
                        if (i + 1 < args.length) d = args[++i]; // dimensao
                }
            }

            if (f == null) {
                System.out.println("Erro: parâmetro obrigatório -f");
                System.exit(1);
            }

            if (OUT_WRITER == null) {
                System.out.println("Erro: parâmetro obrigatório -o");
                System.exit(1);
            }


            int funcionalidade;
            try {
                funcionalidade = Integer.parseInt(f);
            } catch (NumberFormatException ex) {
                OUT_WRITER.println("Erro: o parametro f não é inteiro");
                return;
            }
            //System.out.println("Funcionalidade (-f): " + funcionalidade);
            //System.out.println("Ficheiro A (-a): " + a);
            //System.out.println("Ficheiro B (-b): " + b);
            //System.out.println("Ficheiro E (-e): " + e);
            //System.out.println("Output (-o): " + o);
            //System.out.println("Dimensao (-d): " + d);

            switch (funcionalidade) {
                case 1:
                    if (a == null) {
                        OUT_WRITER.println("O parametro -a não foi fornecido");
                        return;
                    }
                    primeiroExercicioNI(a);
                    break;
                case 2:
                    if (a == null) {
                        OUT_WRITER.println("O parametro -a não foi fornecido");
                        return;
                    }
                    segundoExercicioNI(a);
                    break;
                case 3:
                    if (a == null || b == null) {
                        OUT_WRITER.println("Um dos parametros (a ou b) nao foi fornecido.");
                        return;
                    }
                    terceiroExercicioNI(a, b);
                    break;
                case 4:
                    if (a == null) {
                        OUT_WRITER.println("O parametro -a não foi fornecido");
                        return;
                    }
                    quartoExercicioNI(a);
                    break;
                case 5:
                    if (e == null) {
                        OUT_WRITER.println("O parametro -e não foi fornecido");
                        return;
                    }
                    quintoExercicioNI(e);
                    break;
                case 6:
                    if (Integer.parseInt(d) <= 0 || d == null) {
                        OUT_WRITER.println("A dimensão é invalida ou nao foi fornecida");
                        return;
                    }
                    sextoExercicioNI(Integer.parseInt(d));
                    break;
                case 7:
                    if (Integer.parseInt(d) <= 0 || d == null) {
                        OUT_WRITER.println("A dimensão é invalida ou nao foi fornecida");
                        return;
                    }
                    setimoExercicioNI(Integer.parseInt(d));
                    break;
                case 8:
                    if (a == null || e == null) {
                        OUT_WRITER.println("Um dos parametros (a ou e) nao foi fornecido.");
                        return;
                    }
                    oitavoExercicioNI(a, e);
                    break;
                case 9:
                    if (Integer.parseInt(d) <= 0 || d == null) {
                        OUT_WRITER.println("A dimensão é invalida ou nao foi fornecida");
                        return;
                    }
                    nonoExercicioNI(Integer.parseInt(d));
                    break;
                case 10:
                    if (Integer.parseInt(d) <= 0 || d == null) {
                        OUT_WRITER.println("A dimensão é invalida ou nao foi fornecida");
                        return;
                    }
                    decimoExercicioNI(Integer.parseInt(d));
                    break;
                default:
                    OUT_WRITER.println("-f tem de estar no intervalo 1-10!");
                    System.exit(1);

            }

            if (OUT_WRITER != null) {
                OUT_WRITER.flush();
                OUT_WRITER.close();
            }
        }
    }
}
