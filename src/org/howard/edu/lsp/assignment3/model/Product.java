package org.howard.edu.lsp.assignment3.model;

import java.math.BigDecimal;

/**
 * Domain object representing a single product row.
 * Encapsulates both original and transformed attributes.
 */
public class Product {
    private final int productId;
    private String name;               // transformed to UPPERCASE
    private BigDecimal price;          // final price after any discount + rounding
    private String category;           // may be recategorized to "Premium Electronics"
    private final String originalCategory; // needed to apply conditional rules
    private PriceRange priceRange;     // computed from final price

    public Product(int productId, String name, BigDecimal price, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.originalCategory = category;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public String getOriginalCategory() { return originalCategory; }
    public PriceRange getPriceRange() { return priceRange; }

    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setPriceRange(PriceRange priceRange) { this.priceRange = priceRange; }
}
