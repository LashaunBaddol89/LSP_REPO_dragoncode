package org.howard.edu.lsp.assignment3.transform;

import org.howard.edu.lsp.assignment3.model.PriceRange;
import org.howard.edu.lsp.assignment3.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Concrete Transformer implementing the assignment's transformation rules.
 * Order: uppercase name -> discount electronics -> recategorize if > 500 -> compute price range.
 */
public class ProductTransformer implements Transformer {

    @Override
    public void transform(List<Product> products) {
        for (Product p : products) {
            // (1) Uppercase name
            p.setName(p.getName().toUpperCase());

            // (2) Discount if original category is Electronics
            boolean wasElectronics = "electronics".equalsIgnoreCase(p.getOriginalCategory());
            BigDecimal price = p.getPrice();

            if (wasElectronics) {
                price = price.multiply(new BigDecimal("0.90"));
                price = price.setScale(2, RoundingMode.HALF_UP);
            } else {
                price = price.setScale(2, RoundingMode.HALF_UP);
            }
            p.setPrice(price);

            // (3) Recategorize if > 500 and originally Electronics
            if (wasElectronics && price.compareTo(new BigDecimal("500.00")) > 0) {
                p.setCategory("Premium Electronics");
            }

            // (4) PriceRange from final price
            p.setPriceRange(PriceRange.from(price));
        }
    }
}
