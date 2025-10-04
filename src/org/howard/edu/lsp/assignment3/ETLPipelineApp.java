package org.howard.edu.lsp.assignment3;

import org.howard.edu.lsp.assignment3.io.CsvProductReader;
import org.howard.edu.lsp.assignment3.io.CsvProductWriter;
import org.howard.edu.lsp.assignment3.model.Product;
import org.howard.edu.lsp.assignment3.transform.ProductTransformer;
import org.howard.edu.lsp.assignment3.transform.Transformer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Entry point for Assignment 3.
 * Orchestrates Extract -> Transform -> Load using object-oriented components.
 *
 * Requirements preserved from A2:
 *  - Input:  data/products.csv (relative path)
 *  - Output: data/transformed_products.csv (relative path)
 *  - Transform order and rounding rules unchanged
 *  - Graceful handling for missing and empty input
 *  - Print run summary
 */
public class ETLPipelineApp {

    private static final Path INPUT  = Paths.get("data/products.csv");
    private static final Path OUTPUT = Paths.get("data/transformed_products.csv");

    public static void main(String[] args) {
        CsvProductReader reader = new CsvProductReader(INPUT);
        CsvProductWriter writer = new CsvProductWriter(OUTPUT);
        Transformer transformer = new ProductTransformer();

        List<Product> products;
        try {
            // Extract
            products = reader.readAll();
        } catch (IOException e) {
            System.out.println("ERROR: Could not read input: " + e.getMessage());
            System.out.println("Make sure you're running from the project root and data/products.csv exists.");
            return;
        }

        // Transform (no-op if list empty)
        transformer.transform(products);

        // Load (always writes header)
        try {
            writer.writeAll(products);
        } catch (IOException e) {
            System.out.println("ERROR: Failed to write output: " + e.getMessage());
            return;
        }

        // Summary
        System.out.println();
        System.out.println("=== Run Summary (A3) ===");
        System.out.println("Rows read       : " + reader.getRowsRead());
        System.out.println("Transformed     : " + products.size());
        System.out.println("Skipped         : " + reader.getSkipped());
        System.out.println("Output written  : " + OUTPUT);
    }
}
