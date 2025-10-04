package org.howard.edu.lsp.assignment3.model;

import java.math.BigDecimal;

/**
 * Price range classifier based on the final price.
 */
public enum PriceRange {
    Low, Medium, High, Premium;

    /**
     * Map a final price to a PriceRange using assignment thresholds.
     */
    public static PriceRange from(BigDecimal price) {
        if (price.compareTo(new BigDecimal("10.00")) <= 0) return Low;
        if (price.compareTo(new BigDecimal("100.00")) <= 0) return Medium;
        if (price.compareTo(new BigDecimal("500.00")) <= 0) return High;
        return Premium;
    }
}
