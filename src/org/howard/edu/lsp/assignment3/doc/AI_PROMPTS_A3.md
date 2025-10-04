# AI Prompts & Excerpts (A3)

**Prompt 1:**  
“Redesign my single-file Java ETL (CSV → transform → CSV) into an OO structure using separate classes and an interface for the transform step. Keep exact behavior the same.”

**Excerpt:**  
“Create `Product`, `PriceRange` (enum), `CsvProductReader`, `CsvProductWriter`, `Transformer` interface, and `ProductTransformer`. Use an `ETLPipelineApp` to orchestrate Extract → Transform → Load.”

**How I used it:**  
Kept the class split and interface idea; implemented exact assignment rules and rounding.

---

**Prompt 2:**  
“Show me how to keep the transformation order identical: uppercase → 10% discount for Electronics → recategorize if >$500 → compute PriceRange.”

**Excerpt:**  
“Perform in that order per row; use BigDecimal with HALF_UP rounding to 2 decimals after discount.”

**How I used it:**  
Applied precisely in `ProductTransformer`.

---

**Prompt 3:**  
“How should I structure Javadocs for each class and public method?”

**Excerpt:**  
“Add class-level descriptions (responsibility) and method-level docs (purpose, parameters, exceptions).”

**How I used it:**  
Added concise Javadocs across model, io, transform, and app classes.
