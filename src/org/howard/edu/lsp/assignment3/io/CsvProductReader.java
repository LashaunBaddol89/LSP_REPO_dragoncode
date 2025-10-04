package org.howard.edu.lsp.assignment3.io;

import org.howard.edu.lsp.assignment3.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Extract step: reads data/products.csv using relative path.
 * Skips malformed rows, counts rows read, and returns parsed products.
 */
public class CsvProductReader {
    private final Path inputPath;

    private int rowsRead = 0;
    private int skipped = 0;

    public CsvProductReader(Path inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * Read and parse products from CSV. If file missing, throw IOException (handled by app).
     */
    public List<Product> readAll() throws IOException {
        if (!Files.exists(inputPath)) {
            throw new IOException("Missing input file at: " + inputPath.toString());
        }
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            String line = reader.readLine(); // header
            if (line == null) return products; // empty file without header (treat as empty)

            while ((line = reader.readLine()) != null) {
                rowsRead++;
                String[] parts = line.split(",", -1);
                if (parts.length != 4) { skipped++; continue; }

                String idStr = parts[0].trim();
                String name = parts[1].trim();
                String priceStr = parts[2].trim();
                String category = parts[3].trim();

                try {
                    int productId = Integer.parseInt(idStr);
                    BigDecimal price = new BigDecimal(priceStr);
                    products.add(new Product(productId, name, price, category));
                } catch (NumberFormatException ex) {
                    skipped++;
                }
            }
        }
        return products;
    }

    public int getRowsRead() { return rowsRead; }
    public int getSkipped() { return skipped; }
}
