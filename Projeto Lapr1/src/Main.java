import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import org.apache.commons.math4.legacy.linear.LUDecomposition;
import org.apache.commons.math4.legacy.linear.EigenDecomposition;
import org.apache.commons.math4.legacy.linear.MatrixUtils;
import org.apache.commons.math4.legacy.linear.RealMatrix;
import org.apache.commons.math4.legacy.linear.RealVector;



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
                System.exit(2);
            }

            linhas++;
        }

        if (linhas != colunas) {
            System.out.println("matriz não e quadrada no ficheiro " + filename);
            System.exit(2);
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
                if (matriz[i][j] < 3 && verificarEstavel) {
                    System.out.println("Matriz não é estável! | Ficheiro: " + filename);
                    System.exit(2);
                }
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

    public static int[][] continuacaoEx3(int[][] matrizInicial) {

        HeatmapImageWriter imageWriter = new HeatmapImageWriter();

        final int MAX_MATRIZES = 9999;

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
                    while (matriz[i][j] >= VALOR_CRITICO) {
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

        if (i > 0) grid[i - 1][j]++;
        if (i < grid.length - 1) grid[i + 1][j]++;
        if (j > 0) grid[i][j - 1]++;
        if (j < grid[i].length - 1) grid[i][j + 1]++;
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

            for (int i = 0; i < ordem; i++) {
                double valor = vetorProprio.getEntry(i);
                resultados[k + 1][i] = (Math.round(valor * 1000.0) / 1000.0);
            }
        }

        return resultados;
    }


    public static void imprimirEspectro(double[][] resultados) {

        int ordem = resultados[0].length;

        System.out.println("valores próprios");

        System.out.print("[ ");
        for (int i = 0; i < ordem; i++) {
            System.out.print(resultados[0][i]);
            if (i < ordem - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        System.out.println("vetores proprios");


        for (int i = 1; i <= ordem; i++) {
            System.out.print("[ ");
            for (int j = 0; j < ordem; j++) {
                System.out.print(resultados[i][j]);
                if (j < ordem - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }

    public static void primeiroExercicio(Scanner scanner) throws FileNotFoundException {
        System.out.print("Nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);
        imprimir(matriz);

    }

    public static void segundoExercicio(Scanner scanner) throws FileNotFoundException {
        System.out.print("Nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);

        System.out.println("Matriz Carregada: ");
        imprimir(matriz);

        if (verificarSeEstavel(matriz)) {
            System.out.println("A matriz é estável");
        } else {
            System.out.println("A matriz não é estável");
            estabilizarParticular(matriz);

            System.out.println("Matriz estabilizada: ");
            imprimir(matriz);
            escreverFicheiro(matriz);
        }
    }

    public static void terceiroExercicio(Scanner scanner) throws FileNotFoundException {

        System.out.print("Nome do ficheiro da configuração atual: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matrizA = lerMatriz("input/" + nomeFicheiro, false);

        if (verificarSeEstavel(matrizA)) {
            System.out.print("Nome do ficheiro das novas tarefas: ");
            nomeFicheiro = scanner.nextLine();
            int[][] matrizB = lerMatriz("input/" + nomeFicheiro, false);

            int[][] somaMatrizes = somarMatrizes(matrizA, matrizB);
            continuacaoEx3(somaMatrizes);

        } else {
            System.out.println("A Configuração Atual não é estavel!");
        }
    }

    public static void quartoExercicio(Scanner scanner) throws FileNotFoundException {
        System.out.print("Nome do ficheiro: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matriz = lerMatriz("input/" + nomeFicheiro, false);

        if (verificarSeEstavel(matriz)) {
            if (burningDhar(matriz)) {
                System.out.println("A matriz é recorrente");
            } else {
                System.out.println("A matriz não é recorrente");
            }
        } else {
            System.out.println("A matriz não é estável logo nao posso aplicar o algoritmo burning de dhar");
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


    private static boolean gerarEVerificar(int[][] atual, int pos, int[][] E) {
        int n = atual.length;

        if (pos == n * n) {

            if (!burningDhar(atual)) {
                return true;
            }

            int[][] soma = somarMatrizes(atual, E);
            estabilizarParticular(soma);

            if (!matrizesIguais(atual, soma)) {
                return false;
            }

            return true;
        }

        int i = pos / n;
        int j = pos % n;

        for (int v = 0; v < 4; v++) {
            atual[i][j] = v;

            if (!gerarEVerificar(atual, pos + 1, E)) {
                return false;
            }
        }

        return true;
    }



    public static boolean verificarElementoNeutro(int[][] E) {
        int n = E.length;
        int[][] atual = new int[n][n];

        return gerarEVerificar(atual, 0, E);
    }

    public static void quintoExercicio(Scanner scanner) throws FileNotFoundException {
        System.out.print("Nome do ficheiro que contem a matriz E: ");
        String nomeFicheiro = scanner.nextLine();
        int[][] matrizE = lerMatriz("input/" + nomeFicheiro, false);

        if (!verificarSeEstavel(matrizE)) {
            System.out.println("E não é estável, logo não pode ser elemento neutro.");
            return;
        }

        if (!burningDhar(matrizE)) {
            System.out.println("E não é recorrente, logo não pertence a R.");
            return;
        }

        boolean eNeutro = verificarElementoNeutro(matrizE);

        if (eNeutro) {
            System.out.println("A matriz E é elemento neutro da operação + em R.");
        } else {
            System.out.println("A matriz E não é elemento neutro da operação ⊕ em R.");
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
        System.out.print("Ordem da matriz: ");
        int ordem = Integer.parseInt(scanner.nextLine());

        int total = contarConfiguracoesRecorrentes(ordem);

        System.out.println("Número de configurações recorrentes: " + total);
    }

    public static void setimoExercicio(Scanner scanner) {
        System.out.print("Ordem da matriz: ");
        int ordem = Integer.parseInt(scanner.nextLine());

        int[][] laplaciana = laplacianaReduzida(ordem);
        int determinanteLaplaciana = (int) Math.round(determinanteLaplaciana(laplaciana));

        System.out.println("Numero configurações recorrentes: " + determinanteLaplaciana);
    }

    public static void oitavoExercicio(Scanner scanner) throws FileNotFoundException {

        System.out.print("Nome do ficheiro da matriz A: ");
        String nomeA = scanner.nextLine();

        System.out.print("Nome do ficheiro da matriz E: ");
        String nomeE = scanner.nextLine();

        int[][] A = lerMatriz("input/" + nomeA, false);
        int[][] E = lerMatriz("input/" + nomeE, false);

        if (!burningDhar(A) || !burningDhar(E)) {
            System.out.println("A ou E não são recorrentes.");
            return;
        }
    }


    public static void imprimirMenu() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println();

        int escolha = -1;

        while (escolha != 0) {
            for (int i=0; i<45; i++) System.out.print("-");
            System.out.println();
            System.out.println("[1] Imprimir Matriz do Ficheiro");
            System.out.println("[2] Verificar se a matriz é estável");
            System.out.println("[3] Adicionar tarefas á configuração atual");
            System.out.println("[4] Verificar se a matriz é recorrente");
            System.out.println("[5] Verificar elemento neutro");
            System.out.println("[6] Calcular número de configurações recorrentes sem Laplaciana");
            System.out.println("[7] Calcular número de configurações recorrentes com Laplaciana");
            System.out.println();
            System.out.println("[0] Sair");

            System.out.print("--> ");
            escolha = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (escolha) {
                case 1:
                    primeiroExercicio(scanner);
                    System.out.println();
                    break;
                case 2:
                    segundoExercicio(scanner);
                    System.out.println();
                    break;
                case 3:
                    terceiroExercicio(scanner);
                    System.out.println();
                    break;
                case 4:
                    quartoExercicio(scanner);
                    System.out.println();
                    break;
                case 5:
                    quintoExercicio(scanner);
                    System.out.println();
                case 6:
                    sextoExercicio(scanner);
                    System.out.println();
                    break;
                case 7:
                    setimoExercicio(scanner);
                    System.out.println();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.exit(0);
                    break;
            }
        }
    }



    public static void main(String[] args) throws FileNotFoundException {
        for (int i=0;i < args.length; i++) {
            System.out.println(args[i] + "\n");
        }

        imprimirMenu();

        int[][] A = lerMatriz("input/matrizA", true);
        int[][] B = lerMatriz("input/matrizB", false);

        if (A.length != B.length) {
            System.out.println("matrizes têm ordens diferentes.");
            return;
        }

        System.out.println("Matriz A:");
        imprimir(A);


        System.out.println();
        int[][] soma = somarMatrizes(A, B);
        int[][] matrizEstabilizada = continuacaoEx3(soma);

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
        int determinanteLaplaciana = (int) Math.round(determinanteLaplaciana(matrizLaplaciana)); // duvida aqui (arredondar sempre para cima ou acima de 0.5 arredondar para cima ? (Math.ceil ou Math.round))

        System.out.println("Determinante da Laplaciana = " + determinanteLaplaciana);
        double[][] res = espectroLaplaciana(matrizLaplaciana);

        imprimirEspectro(res);
    }
}
