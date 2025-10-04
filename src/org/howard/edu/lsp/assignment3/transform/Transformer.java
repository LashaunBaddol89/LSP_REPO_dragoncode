package org.howard.edu.lsp.assignment3.transform;

import org.howard.edu.lsp.assignment3.model.Product;

import java.util.List;

/**
 * Transformer interface to enable polymorphism and future swaps of strategies.
 */
public interface Transformer {
    /**
     * Transform the provided list in-place, applying all required rules
     * in the prescribed order.
     */
    void transform(List<Product> products);
}
