# SGA1 to SGA2 schema migration

## Baseline
The SGA1 Project 4 database contained 15 tables: `customers`, `customer_addresses`, `sellers`, `categories`, `products`, `carts`, `cart_items`, `orders`, `order_items`, `payments`, `shipments`, `product_returns`, `return_items`, `product_reviews`, and `inventory_movements`.

## Required platform change
SGA2 explicitly mandates MySQL and JPA/Hibernate. The SQL syntax was therefore translated from PostgreSQL 14+ to MySQL 8 / InnoDB. The domain tables, keys, relationships, key checks, generated totals, and business meaning are retained.

## Structural portability change
PostgreSQL used a partial unique index to ensure no customer can have more than one OPEN cart. MySQL does not support partial unique indexes. The MySQL schema adds a stored generated column `carts.open_customer_id`:

```sql
CASE WHEN status = 'OPEN' THEN customer_id ELSE NULL END
```

A unique constraint on that column enforces one open cart per customer while allowing multiple closed/converted cart records because MySQL unique indexes permit multiple `NULL` values.

## Implementation-level mapping
`cart_items`, `order_items`, `return_items`, and `product_reviews` are association entities, not bare join tables, because the relationships carry domain attributes (quantity, purchase-time unit price, return quantity, rating/review text). This preserves each SGA1 relationship and its attributes while representing the corresponding many-to-many associations.

## What was not added
No extra business tables were introduced. The application uses the same 15-table model. Demo seed data is stored as Flyway V2 migration data and is not a schema addition.
