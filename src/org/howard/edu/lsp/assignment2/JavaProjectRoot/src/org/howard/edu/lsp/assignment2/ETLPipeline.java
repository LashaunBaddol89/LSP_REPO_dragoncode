package org.howard.edu.lsp.assignment2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CSCI 363/540 – Assignment 1: CSV ETL Pipeline in Java
 *
 * Reads data/products.csv, applies required transforms, and writes data/transformed_products.csv.
 * Run from the project root (the folder that contains /src and /data).
 *
 * AI usage note (example, customize for your README):
 * - I used an AI assistant to help outline the steps and produce a first-pass implementation.
 * - Prompt: "Write a small Java ETL reading CSV, transforming, writing CSV with BigDecimal HALF_UP."
 * - I reviewed and adapted the output: added robust error handling, precise rounding, and exact transform order.
 */
public class ETLPipeline {

    // --- Constants required by the spec ---
    private static final String INPUT_FILE  = "data/products.csv";
    private static final String OUTPUT_FILE = "data/transformed_products.csv";
    private static final String OUTPUT_HEADER = "ProductID,Name,Price,Category,PriceRange";

    // Run summary counters
    private static class Summary {
        int rowsRead = 0;         // data rows encountered (not counting header)
        int transformed = 0;      // rows successfully transformed and written
        int skipped = 0;          // malformed or invalid rows
    }

    // Simple record to hold a row (after parsing; before/after transform)
    private static class Product {
        int productId;
        String name;
        BigDecimal price;     // current/possibly discounted price
        String category;
        String priceRange;    // computed from final price
        String originalCategory; // to check recategorization rule

        Product(int productId, String name, BigDecimal price, String category) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.category = category;
            this.originalCategory = category; // keep original for rule check
        }
    }

    public static void main(String[] args) {
        Summary summary = new Summary();
        PrintStream out = System.out;

        Path inputPath  = Paths.get(INPUT_FILE);
        Path outputPath = Paths.get(OUTPUT_FILE);

        // 1) EXTRACT
        List<Product> products;
        try {
            products = extract(inputPath, summary);
        } catch (MissingInputFileException e) {
            out.println("ERROR: Could not find input file at '" + INPUT_FILE + "'.");
            out.println("Make sure you run from the project root and that data/products.csv exists.");
            return; // graceful exit
        } catch (IOException e) {
            out.println("ERROR: Failed to read input file: " + e.getMessage());
            return;
        }

        // 2) TRANSFORM
        transform(products, summary);

        // 3) LOAD
        try {
            load(outputPath, products, summary);
        } catch (IOException e) {
            out.println("ERROR: Failed to write output file: " + e.getMessage());
            return;
        }

        // Print run summary
        out.println();
        out.println("=== Run Summary ===");
        out.println("Rows read       : " + summary.rowsRead);
        out.println("Transformed     : " + summary.transformed);
        out.println("Skipped         : " + summary.skipped);
        out.println("Output written  : " + OUTPUT_FILE);
    }

    /**
     * Extract: read CSV (relative path), skip header, parse rows.
     * Rules:
     *  - If file missing -> throw MissingInputFileException (handled in main).
     *  - If empty (only header) -> still proceed; load() will write just header.
     *  - If a row is malformed (wrong columns or parse error) -> count as skipped.
     */
    private static List<Product> extract(Path inputPath, Summary summary) throws IOException, MissingInputFileException {
        if (!Files.exists(inputPath)) {
            throw new MissingInputFileException();
        }

        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            String line = reader.readLine(); // header
            if (line == null) {
                // No header at all; treat as empty with no rows.
                return products;
            }

            // Optional: verify header has at least 4 columns (not strictly required by spec)
            // We won't transform header; just skip to data lines.
            while ((line = reader.readLine()) != null) {
                summary.rowsRead++;

                String[] parts = line.split(",", -1); // keep empty fields if any
                if (parts.length != 4) {
                    summary.skipped++;
                    continue;
                }

                String idStr = parts[0].trim();
                String name  = parts[1].trim();
                String priceStr = parts[2].trim();
                String category = parts[3].trim();

                try {
                    int productId = Integer.parseInt(idStr);
                    BigDecimal price = new BigDecimal(priceStr);
                    products.add(new Product(productId, name, price, category));
                } catch (NumberFormatException ex) {
                    summary.skipped++;
                }
            }
        }

        return products;
    }

    /**
     * Transform each product in-place in the exact order:
     *  (1) uppercase name
     *  (2) apply 10% discount if original category == "Electronics" (case-insensitive),
     *      then round to 2 decimals, HALF_UP
     *  (3) if post-discount price > 500.00 AND original category was "Electronics",
     *      set category to "Premium Electronics"
     *  (4) compute PriceRange from final price
     */
    private static void transform(List<Product> products, Summary summary) {
        for (Product p : products) {
            // (1) Uppercase name
            p.name = p.name.toUpperCase();

            // (2) Discount if Electronics (based on original category)
            boolean wasElectronics = "electronics".equalsIgnoreCase(p.originalCategory);
            if (wasElectronics) {
                // price * 0.90 and round HALF_UP to 2 decimals
                p.price = p.price.multiply(new BigDecimal("0.90"));
                p.price = p.price.setScale(2, RoundingMode.HALF_UP);
            } else {
                // still normalize to 2 decimals (if not already), but spec doesn't require.
                p.price = p.price.setScale(2, RoundingMode.HALF_UP);
            }

            // (3) Recategorize if needed
            if (wasElectronics && p.price.compareTo(new BigDecimal("500.00")) > 0) {
                p.category = "Premium Electronics";
            } else {
                // leave the (possibly original) category untouched
                // but ensure capitalization matches original input (spec doesn't force change)
                // We'll keep as-is.
            }

            // (4) PriceRange from final price
            p.priceRange = computePriceRange(p.price);

            summary.transformed++;
        }
    }

    private static String computePriceRange(BigDecimal price) {
        // boundaries:
        // 0.00–10.00 → Low
        // 10.01–100.00 → Medium
        // 100.01–500.00 → High
        // 500.01+ → Premium
        BigDecimal p = price;

        if (p.compareTo(new BigDecimal("10.00")) <= 0) {
            return "Low";
        } else if (p.compareTo(new BigDecimal("100.00")) <= 0) {
            return "Medium";
        } else if (p.compareTo(new BigDecimal("500.00")) <= 0) {
            return "High";
        } else {
            return "Premium";
        }
    }

    /**
     * Load: write header + transformed rows to data/transformed_products.csv
     * If there were zero transformed rows (e.g., empty input), we still write the header.
     */
    private static void load(Path outputPath, List<Product> products, Summary summary) throws IOException {
        // Ensure data/ folder exists; create if missing
        if (outputPath.getParent() != null && !Files.exists(outputPath.getParent())) {
            Files.createDirectories(outputPath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write(OUTPUT_HEADER);
            writer.newLine();

            for (Product p : products) {
                // Column order: ProductID,Name,Price,Category,PriceRange
                writer.write(p.productId + ","
                        + p.name + ","
                        + p.price.toPlainString() + ","
                        + p.category + ","
                        + p.priceRange);
                writer.newLine();
            }
            writer.flush();
        }
    }

    // Custom exception to clearly separate “file missing” from other IO problems
    private static class MissingInputFileException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
