# E-Commerce Order and Inventory Management

**SGA2 | Building Database Applications | Project 4**  
A Spring Boot + Maven modular monolith built on the SGA1 e-commerce relational model.

## Stack

- Java 21, Spring Boot 3.3, Maven
- MySQL 8, Spring Data JPA / Hibernate, Flyway migrations
- Spring MVC, JSP, JSTL, HTML/CSS, JavaScript, jQuery and AJAX
- JUnit 5, Mockito, Spring Boot Test, H2 (test profile only)

## Architecture

The code is a feature-first modular monolith. Each business module owns its controller, DTO, mapper, model, repository, and service packages. Controllers depend on service interfaces, business rules live in services, repositories own persistence and custom queries, and JSPs are presentation-only.

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

## Run locally

Requirements: JDK 21, Docker Desktop (or MySQL 8), and Maven 3.9+ (the included `./mvnw` downloads Maven if it is not installed).

```bash
docker compose up -d mysql
./mvnw clean verify
./mvnw spring-boot:run
```

Open `http://localhost:8080`. The demo seed includes customer ID `1` and a default address ID `1` for checkout. No real payment gateway is connected; checkout records a demo COD payment so transactional order and inventory behavior can be evaluated safely.

Default local database settings are in `application.yml` and can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. Change credentials before any non-local deployment.

## Tests

```bash
./mvnw test
```

Tests use an isolated H2 database in MySQL compatibility mode. Application runtime and Flyway use MySQL 8.

## Database notes

The original SGA1 schema was designed for PostgreSQL. SGA2 mandates MySQL, so `V1__baseline_schema.sql` carries the original 15-table domain model into MySQL. The only structural portability addition is a generated `open_customer_id` column + unique index on carts to preserve the SGA1 rule of at most one OPEN cart per customer (MySQL does not support PostgreSQL partial unique indexes). Generated totals, foreign-key ownership, stock movement auditing, return/payment linkage, and the association tables remain represented.

## Main workflows

1. Browse/filter products, add to cart using AJAX, see a live stock check, then checkout.
2. Checkout snapshots unit prices, validates cart/address/stock, decrements stock, records inventory movements, writes a payment and creates a shipment in one transaction.
3. Submit a return request, approve/reject it, then receive an approved return. Receipt restores stock and writes a linked refund transactionally.

## Repository layout

```text
com.lowkeyarhan.ecommerce
  common/       shared validation, exception handling and MVC configuration
  customer/     customer + address domain
  catalog/      seller, category and product domain
  cart/         open carts and cart-item associations
  orders/       order, order-item, payment, shipment and return workflows
  inventory/    inventory movement ledger and restocking
  reviews/      purchase-qualified product reviews
  reports/      dashboard, custom query projections and report views
src/main/resources
  db/migration/ Flyway MySQL schema and sample data
  webapp/WEB-INF/jsp/ JSP views secured from direct access
  static/       CSS and JavaScript assets
```

## Important demo constraints

- The project is an academic application, not a production checkout system.
- Customer selection is intentionally a demo selector; do not expose this unauthenticated setup publicly.
- No payment processor, email provider, or external shipping integration is configured.
