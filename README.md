# E-Commerce Order and Inventory Management

**SGA2 · Project 04 · Full-Stack Database Application Development**

| | |
|---|---|
| Student | Arhan Das |
| BITS ID | 2024EB02386 |
| Institution | Scaler School of Technology, Bengaluru |
| Course | Building Database Applications |
| Database | MySQL 8 (migrated from the SGA1 PostgreSQL model) |
| Stack | Java 21 · Spring Boot 3.3.5 · Maven · JPA/Hibernate · JSP/JSTL · jQuery |
| Report | [`2024EB02386_ECommerce_SGA2_Report.pdf`](2024EB02386_ECommerce_SGA2_Report.pdf) |

A Spring Boot modular monolith that turns the SGA1 15-table e-commerce relational design into a working web application: multi-seller catalogue, customers and addresses, carts, checkout, inventory movement auditing, order history, returns, refunds and database-backed reports.

## Features

| Area | Functions |
|---|---|
| Catalogue | Product, category and seller CRUD; keyword/category search; pagination; stock inspection |
| Customers | Customer and address CRUD with validated contact details |
| Cart | One OPEN cart per customer; add/update/remove items; AJAX add-to-cart and live stock lookup |
| Orders | Transactional checkout, price snapshots, stock deduction, demo COD payment and shipment, paginated order history |
| Returns | Request returns for delivered orders, approve/reject, receive items, linked refund and stock restoration |
| Inventory | Restock products and record sale/restock/return/adjustment movement ledger entries |
| Dashboard & reports | Low-stock, monthly net sales and top-selling products reports |

Out of scope: real payment gateway, authentication/authorization, email notifications, carrier integration, multiple warehouses and recommendations. The customer selector and COD payment are deliberate demo shortcuts.

## Stack

- **Language / build:** Java 21, Maven (with Maven Wrapper)
- **Application:** Spring Boot 3.3.5, Spring MVC
- **Persistence:** Spring Data JPA / Hibernate, MySQL 8 / InnoDB, Flyway migrations, HikariCP
- **Views:** JSP + JSTL, CSS, JavaScript, jQuery 3.7.1, AJAX
- **Tests:** JUnit 5, Spring Boot Test, MockMvc, H2 (test profile only)
- **Packaging:** WAR (JSP-compatible, embedded Tomcat)

## Architecture

A feature-first modular monolith. Each module owns its controller, DTO, mapper, entity, repository and service packages; controllers depend on service interfaces, and business rules live in services.

```text
Browser (JSP + CSS + jQuery/AJAX)
              |
        Spring MVC routes
              |
       Feature controllers
              |
       Service interfaces
              |
    Domain service implementations
              |
       Spring Data repositories
              |
     JPA / Hibernate mappings
              |
      MySQL 8 (Flyway schema)
```

Diagrams: [`docs/architecture.dot`](docs/architecture.dot) and [`docs/er_diagram.dot`](docs/er_diagram.dot).

## Database

The SGA1 schema was designed for PostgreSQL; SGA2 requires MySQL, so [`V1__baseline_schema.sql`](src/main/resources/db/migration/V1__baseline_schema.sql) ports the unchanged 15-table domain model to MySQL 8 and [`V2__sample_data.sql`](src/main/resources/db/migration/V2__sample_data.sql) adds deterministic demo data.

The only structural portability change is a generated `open_customer_id` column plus a unique index on carts, preserving the SGA1 rule of at most one OPEN cart per customer (MySQL has no partial unique indexes). Generated order totals, foreign-key ownership, stock movement auditing, return/payment linkage and association entities are retained. Hibernate runs with `ddl-auto: validate`; Flyway owns the schema. Details: [`docs/schema-changes.md`](docs/schema-changes.md).

## Project structure

```text
com.lowkeyarhan.ecommerce
  common/       shared validation, error handling, configuration
  customer/     customer and address domain
  catalog/      seller, category and product domain
  cart/         open carts and cart-item associations
  orders/       orders, payments, shipments and returns
  inventory/    inventory movement ledger and restocking
  reviews/      purchase-qualified product reviews
  reports/      dashboard, query projections and report views
src/main/resources
  application.yml
  db/migration/             Flyway schema and sample data
  static/                   CSS and JavaScript assets
src/main/webapp/WEB-INF/jsp/  JSP views and fragments (not publicly addressable)
src/test/java/              unit, repository query and endpoint tests
docs/                       diagrams, requirements, test cases, guide, evidence
```

## Run locally

Requirements: JDK 21, Docker Desktop (or MySQL 8), and internet access for the Maven Wrapper.

```bash
docker compose up -d mysql
./mvnw clean verify
./mvnw spring-boot:run
```

Open <http://localhost:8080>. The seed data includes customer ID `1` and address ID `1` for demo checkout.

Configuration is read from `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`, with local-only fallbacks in [`application.yml`](src/main/resources/application.yml). Replace the default credentials before any non-local deployment.

## Routes

| HTTP | URL | Purpose |
|---|---|---|
| GET | `/` | Dashboard |
| GET | `/products` | Product listing / search |
| GET, POST | `/products/new`, `/products/save` | Create product |
| GET, POST | `/products/{id}/edit`, `/products/{id}/delete` | Edit / delete product |
| GET | `/api/products/{id}/stock` | AJAX stock lookup |
| GET, POST | `/categories`, `/categories/save` | Category listing and save |
| GET, POST | `/sellers`, `/sellers/save` | Seller listing and save |
| GET, POST | `/customers`, `/customers/save` | Customer listing and save |
| GET, POST | `/cart`, `/api/cart/items` | Cart view and AJAX add-to-cart |
| POST | `/cart/items/{id}/update`, `/cart/items/{id}/remove` | Update / remove cart item |
| GET, POST | `/orders`, `/orders/checkout` | Order listing and transactional checkout |
| GET | `/orders/history`, `/orders/{id}` | Customer order history and detail |
| GET, POST | `/returns`, `/returns/new`, `/returns/{id}/action` | Return queue, request and processing |
| GET, POST | `/inventory`, `/inventory/restock` | Movement ledger and restock |
| GET | `/reports`, `/reports/low-stock`, `/reports/monthly-sales`, `/reports/top-products` | Reports |
| GET | `/api/health` | Health response |

Full table: [`docs/url-mappings.md`](docs/url-mappings.md).

## Core workflows

**Checkout.** The service locks the customer's open cart, verifies it is non-empty and that the shipping address belongs to the customer, then locks products in stable product-id order. It snapshots unit prices into order items, decrements stock, writes SALE inventory movements, creates a demo COD charge and shipment, and converts the cart in one transaction.

**Return and refund.** A return request is validated against the unreturned purchased quantity. Approval and rejection are separate transitions. On receipt the service locks the return and order, enforces the refund ceiling against the original completed charge, restores stock, writes RETURN movements, saves a linked REFUND payment and updates the order state atomically.

**Restock.** Locks the product, applies the positive quantity change and writes a RESTOCK ledger entry in the same transaction.

## Validation and error handling

Form DTOs use Jakarta Bean Validation; services enforce the remaining business rules (ownership, stock, return and refund limits). Controllers inspect `BindingResult` and return user-facing messages. `BusinessRuleException` and `ResourceNotFoundException` are mapped by a global handler to a shared error view instead of exposing stack traces. Client-side HTML5/jQuery checks are usability aids, not a security boundary.

## Tests

```bash
./mvnw test
```

Automated coverage: four business-rule unit tests, two repository query tests and three MockMvc endpoint tests (health, live stock, AJAX cart insertion). They run against H2 in MySQL compatibility mode; runtime and Flyway use MySQL 8. The twelve-case functional plan is in [`docs/functional-test-cases.md`](docs/functional-test-cases.md).

> **Verification status:** automated tests and the MySQL runtime were **not executed** in the authoring environment (Maven repository DNS, Docker and MySQL were unavailable). No compile, migration or test pass is claimed here. Run the commands above locally and record the results before treating the build as verified.

## Documentation

- [User guide](docs/user-guide.md)
- [Requirements matrix](docs/requirements-matrix.md)
- [Functional test cases](docs/functional-test-cases.md)
- [URL mappings](docs/url-mappings.md)
- [Schema changes](docs/schema-changes.md)
- [UI previews](docs/screenshots/)
- [Full project report (PDF)](2024EB02386_ECommerce_SGA2_Report.pdf)

## Git history

The repository holds incremental commits reconstructed from the final source, plus three local feature branches merged into `main` (`feature/catalog-management`, `feature/commerce-workflows`, `feature/reporting-and-quality`). These are Git merge commits, not GitHub pull-request records.

## Limitations

Academic coursework, not a production commerce system: no authentication, no payment gateway, no email or shipping integration, and the customer selector is a demo device only. Do not expose this deployment publicly. See the report's *Conclusion and Future Enhancements* for the planned improvements.
