package com.example.crochet_generator.service;

import com.example.crochet_generator.model.CrochetGrid;
import com.example.crochet_generator.model.PatternResponse;
import com.example.crochet_generator.model.ScaleMode;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrochetPatternService {

    public PatternResponse generateFromImage(InputStream imageStream, int targetWidth, ScaleMode mode, int threshold) throws IOException {
        if (targetWidth < 5 || targetWidth > 150) {
            throw new IllegalArgumentException("Width must be between 5 and 150 stitches.");
        }

        BufferedImage original = ImageIO.read(imageStream);
        if (original == null) {
            throw new IllegalArgumentException("Uploaded file is not a valid image.");
        }
        if (original.getWidth() > 4000 || original.getHeight() > 4000) {
            throw new IllegalArgumentException("Image dimensions cannot exceed 4000x4000 pixels.");
        }

        // Preserve aspect ratio
        double scale = (double) original.getWidth() / targetWidth;
        int targetHeight = Math.max(1, (int) Math.round(original.getHeight() / scale));

        BufferedImage resized = resizeImage(original, targetWidth, targetHeight, mode);
        CrochetGrid grid = convertToGrid(resized, threshold);

        // --- THIS IS THE ROW LOOP ---
        List<String> instructions = new ArrayList<>();
        int height = grid.getHeight();

        // Start from the bottom row of the image (crochetRow = 1) and work upward
        for (int crochetRow = 1; crochetRow <= height; crochetRow++) {
            int imageRow = height - crochetRow;
            boolean rightToLeft = (crochetRow % 2 == 1); // Odd rows: Right-to-Left (RS)
            instructions.add(formatRow(grid, imageRow, crochetRow, rightToLeft));
        }
        // ----------------------------

        return new PatternResponse(instructions, grid.getCells());
    }

    private BufferedImage resizeImage(BufferedImage original, int width, int height, ScaleMode mode) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();

        // Fill background white in case the uploaded PNG has transparency
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        Object hint = (mode == ScaleMode.SHARP)
                ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                : RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);

        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }

    private CrochetGrid convertToGrid(BufferedImage image, int threshold) {
        int height = image.getHeight();
        int width = image.getWidth();
        boolean[][] cells = new boolean[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = new Color(image.getRGB(col, row));
                int averageBrightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                cells[row][col] = averageBrightness < threshold;
            }
        }
        return new CrochetGrid(cells);
    }

    public String formatRow(CrochetGrid grid, int imageRow, int crochetRow, boolean rightToLeft) {
        int width = grid.getWidth();
        String directionLabel = rightToLeft ? " (RS ←): " : " (WS →): ";
        StringBuilder result = new StringBuilder("Row " + crochetRow + directionLabel);

        int startCol = rightToLeft ? width - 1 : 0;
        int step = rightToLeft ? -1 : 1;

        boolean currentType = grid.isFilled(imageRow, startCol);
        int count = 1;

        for (int i = 1; i < width; i++) {
            int col = startCol + (i * step);
            boolean cell = grid.isFilled(imageRow, col);
            if (cell == currentType) {
                count++;
            } else {
                result.append(count).append(currentType ? " filled, " : " open, ");
                currentType = cell;
                count = 1;
            }
        }

        // Flush the final streak
        result.append(count).append(currentType ? " filled" : " open");
        return result.toString();
    }
}