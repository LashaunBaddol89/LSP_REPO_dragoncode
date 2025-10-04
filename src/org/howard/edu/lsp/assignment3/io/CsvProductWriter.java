package org.howard.edu.lsp.assignment3.io;

import org.howard.edu.lsp.assignment3.model.Product;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Load step: writes transformed products to data/transformed_products.csv with header.
 */
public class CsvProductWriter {
    private static final String OUTPUT_HEADER = "ProductID,Name,Price,Category,PriceRange";
    private final Path outputPath;

    public CsvProductWriter(Path outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Write header + rows. Creates data/ folder if missing. Always writes header.
     */
    public void writeAll(List<Product> products) throws IOException {
        if (outputPath.getParent() != null && !Files.exists(outputPath.getParent())) {
            Files.createDirectories(outputPath.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write(OUTPUT_HEADER);
            writer.newLine();
            for (Product p : products) {
                writer.write(
                        p.getProductId() + "," +
                        p.getName() + "," +
                        p.getPrice().toPlainString() + "," +
                        p.getCategory() + "," +
                        p.getPriceRange()
                );
                writer.newLine();
            }
            writer.flush();
        }
    }
}
