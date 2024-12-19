package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.model.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PgmLoadHandler extends BaseImageLoadHandler {

    private String readNonCommentLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
            line = line.trim();
        } while (line.startsWith("#") || line.isEmpty());
        return line;
    }

    @Override
    protected Image handleLoad(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = readNonCommentLine(reader);
            if (!line.equals("P2")) {
                return null; // Non è un file PGM
            }

            line = readNonCommentLine(reader);
            Scanner scanner = new Scanner(line);
            int width = scanner.nextInt();
            int height = scanner.nextInt();

            line = readNonCommentLine(reader);
            int maxGray = Integer.parseInt(line);

            int[][] pixels = new int[height][width];
            for (int y = 0; y < height; y++) {
                line = readNonCommentLine(reader);
                scanner = new Scanner(line);
                for (int x = 0; x < width; x++) {
                    pixels[y][x] = (scanner.nextInt() * 255) / maxGray;
                }
            }
            return new Image(width, height, pixels, "PGM", file);
        }
    }
}
