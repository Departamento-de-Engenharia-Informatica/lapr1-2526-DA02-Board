import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;

public class HeatmapImageWriter {

    /**
     * Writes a 2D array as a heatmap image, mapping:
     * 0 -> White
     * 1 -> Yellow
     * 2 -> Brown
     * 3 -> Red
     * >3 -> Blue
     *
     * @param array the 2D array of integers representing values.
     * @param outputFilePath the file path to save the image.
     * @throws IOException if an error occurs during writing the image.
     */
    public void writeArrayAsImage(int[][] array, String outputFilePath) throws IOException {
        int height = array.length;
        int width = array[0].length;

        // Create an RGB image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Set pixel colors according to the mapping
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = array[y][x];
                Color color;

                switch (value) {
                    case 0:
                        color = Color.WHITE;
                        break;
                    case 1:
                        color = Color.YELLOW;
                        break;
                    case 2:
                        color = new Color(150, 75, 0); // Brown (não existe no Color padrão)
                        break;
                    case 3:
                        color = Color.RED;
                        break;
                    default:
                        color = Color.BLUE;
                        break;
                }

                image.setRGB(x, y, color.getRGB());
            }
        }

        // Save image
        File outputFile = new File(outputFilePath);
        ImageIO.write(image, "png", outputFile);
    }
}

