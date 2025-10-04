# Reflection – Assignment 3 vs Assignment 2

## What changed
A2 was one class with grouped methods. A3 decomposes into cohesive classes:
- `CsvProductReader` (Extract)
- `ProductTransformer` via `Transformer` interface (Transform)
- `CsvProductWriter` (Load)
- `Product`, `PriceRange` (Domain model)
- `ETLPipelineApp` (Orchestrator)

## Why A3 is more OO
- **Objects & Classes:** `Product` is a first-class domain object.
- **Encapsulation:** IO and transform logic hidden behind small APIs.
- **Polymorphism:** `Transformer` interface allows swapping strategies.
- **SRP / Open–Closed:** Each class has one reason to change; new formats/transformers can be added without modifying the app.
- **(Inheritance):** Not required here; interface + composition fit better.

## Same behavior as A2
- Transform order: UPPERCASE → 10% discount for Electronics (HALF_UP to 2 decimals) → recategorize if > $500 → compute PriceRange.
- Produced identical output for Case A.
- Case B: header-only output.
- Case C: clear error, no crash.
- Relative paths preserved.

## Testing
- Reused A2 inputs and compared outputs.
- Verified summary counts: rows read, transformed (list size), skipped.
- Ran from project root; checked file paths and rounding.

## Coding standards & docs
- One public class per file.
- Javadocs for classes and public methods.
- Consistent naming/formatting.

## AI usage (summary)
Used a generative AI assistant to brainstorm OO decomposition, draft class skeletons/Javadocs, and double-check rule ordering/rounding. I reviewed and adapted all suggestions to match the assignment constraints and my style.
