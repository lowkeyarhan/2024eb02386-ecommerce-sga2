# Local user guide

## Start the application

1. Install JDK 21 and Docker Desktop (or install MySQL 8 locally).
2. From the project root, start the MySQL service:

   ```bash
   docker compose up -d mysql
   ```

3. Build and run the app:

   ```bash
   ./mvnw clean verify
   ./mvnw spring-boot:run
   ```

4. Open `http://localhost:8080`.

Flyway creates the 15-table schema and loads a small demo dataset. Demo customer ID `1` has default shipping address ID `1` and an open cart.

## Main navigation

- **Products**: create, edit and delete products; filter by keyword, category and seller; choose a demo customer; use AJAX stock lookup and add-to-cart.
- **Categories / Sellers / Customers**: manage the catalogue and customer records. Product/customer lists are paginated and sorted.
- **Cart**: update/remove cart items and select a customer-owned shipping address.
- **Orders**: review orders, inspect line-item price snapshots, payments and shipment tracking. The history page filters by demo customer.
- **Returns**: create a return request from a delivered order, then use the return queue to approve/reject it and receive approved items.
- **Inventory**: restock stock with a required positive quantity. Every change is recorded in the inventory movement ledger.
- **Reports**: low-stock, monthly net sales and top-selling products.

## Checkout and returns

Checkout is a demo flow. It calculates 18% tax and a ₹49 shipping fee below ₹1,999 (free shipping at/above ₹1,999). It creates a simulated COD payment record and shipment record. No real payment is processed and no carrier is contacted.

The return queue allows an administrator to approve or reject requested items. Receiving an approved return restores stock, adds a RETURN ledger entry, and records a linked refund payment. The server checks the original charge and the amount already refunded.

## Demo-only access boundary

This project does not implement user login or authorization. Customer selection and administrative actions are intentionally open for a local academic demonstration. Do not deploy it to a public network without adding authentication, authorization, CSRF policy review and production secrets management.
