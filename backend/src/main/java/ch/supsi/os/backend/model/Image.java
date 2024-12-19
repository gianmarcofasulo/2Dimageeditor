package ch.supsi.os.backend.model;

import java.io.File;

public class Image {
    private int width;
    private int height;
    private int[][] pixels; // Matrice dei pixel
    private String format; // Formato dell'immagine: PGM, PPM, PBM
    private File file; // Nome del file

    public Image(int width, int height, int[][] pixels, String format, File file) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.format = format;
        this.file = file;
    }

    public Image(int width, int height, int[][] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
