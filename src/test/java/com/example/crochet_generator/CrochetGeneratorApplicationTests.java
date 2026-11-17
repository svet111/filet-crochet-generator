package com.example.crochet_generator;

import com.example.crochet_generator.model.CrochetGrid;
import com.example.crochet_generator.model.PatternResponse;
import com.example.crochet_generator.model.ScaleMode;
import com.example.crochet_generator.service.CrochetPatternService;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CrochetPatternServiceTest {

    private final CrochetPatternService service = new CrochetPatternService();

    @Test
    void formatRow_AlternatesDirectionProperly() {
        // 1 row, 4 columns: [filled, filled, open, filled]
        boolean[][] cells = {{true, true, false, true}};
        CrochetGrid grid = new CrochetGrid(cells);

        // Odd crochetRow (1) reads Right-to-Left: [true, false, true, true]
        String oddRow = service.formatRow(grid, 0, 1, true);
        assertEquals("Row 1 (RS ←): 1 filled, 1 open, 2 filled", oddRow);

        // Even crochetRow (2) reads Left-to-Right: [true, true, false, true]
        String evenRow = service.formatRow(grid, 0, 2, false);
        assertEquals("Row 2 (WS →): 2 filled, 1 open, 1 filled", evenRow);
    }

    @Test
    void generateFromImage_CreatesBottomToTopPattern() throws Exception {
        // Create a 10x10 image: top half white, bottom half black
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 10, 5);
        g.setColor(Color.BLACK);
        g.fillRect(0, 5, 10, 5);
        g.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "png", out);
        InputStream stream = new ByteArrayInputStream(out.toByteArray());

        PatternResponse response = service.generateFromImage(stream, 10, ScaleMode.SHARP, 128);

        assertEquals(10, response.instructions().size());
        // Row 1 is the bottom row (black -> filled)
        assertEquals("Row 1 (RS ←): 10 filled", response.instructions().get(0));
        // Row 10 is the top row (white -> open)
        assertEquals("Row 10 (WS →): 10 open", response.instructions().get(9));
    }

    @Test
    void generateFromImage_RejectsInvalidWidth() {
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () ->
                service.generateFromImage(emptyStream, 200, ScaleMode.SMOOTH, 128)
        );
    }
}