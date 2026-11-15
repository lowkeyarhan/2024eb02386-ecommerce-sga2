# Functional test cases

The following cases are the manual end-to-end checklist. They are intentionally not marked as passed until run against the deployed application and MySQL database.

| ID | Description | Input | Expected result | Actual result | Status |
|---|---|---|---|---|---|
| FT-01 | Product catalogue loads | GET `/products` | Product list, filters and pagination render | Not executed in this environment | Pending |
| FT-02 | Search and filter catalogue | Keyword `USB`, category filter | Only matching products appear | Not executed in this environment | Pending |
| FT-03 | Add item to cart using AJAX | Product ID, quantity 1 | Cart count/subtotal update without reload | Not executed in this environment | Pending |
| FT-04 | Live stock lookup | GET `/api/products/{id}/stock` | Current quantity and active status returned | Not executed in this environment | Pending |
| FT-05 | Checkout with valid cart | Customer 1, address 1 | Order, payment, shipment and stock ledger written atomically | Not executed in this environment | Pending |
| FT-06 | Checkout with insufficient stock | Quantity above current stock | Error shown; no order or stock mutation committed | Not executed in this environment | Pending |
| FT-07 | Reject foreign customer address | Customer 1 with another customer's address | Checkout rejected; no order created | Not executed in this environment | Pending |
| FT-08 | Submit return request | Delivered order item and valid quantity | Return created in REQUESTED state | Not executed in this environment | Pending |
| FT-09 | Receive approved return | Approved return with sufficient original charge | Inventory restored and refund payment linked to charge | Not executed in this environment | Pending |
| FT-10 | Reject excess return quantity | Requested quantity above remaining purchase quantity | Request rejected and database unchanged | Not executed in this environment | Pending |
| FT-11 | Restock product | Product ID and positive quantity | Stock increases and RESTOCK ledger row is saved | Not executed in this environment | Pending |
| FT-12 | Reports render | Low stock, monthly sales and top products | Three report views show aggregated database data | Not executed in this environment | Pending |

## Automated tests included in the source
- Business-rule unit tests: positive price, positive quantity, available stock and refund ceiling.
- Repository query tests: catalogue keyword/category filtering and low-stock filtering.
- Endpoint smoke tests: `/api/health`, `/api/products/{id}/stock`, and AJAX `POST /api/cart/items`.

The build/test suite could not be executed in the authoring container because Maven was not installed and external Maven artifact downloads were unavailable. The exact local verification commands are `./mvnw clean verify` and `docker compose up -d mysql`. Update the Actual result and Status columns after executing the checklist.
