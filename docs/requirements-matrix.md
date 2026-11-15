# SGA2 coverage matrix

| SGA2 requirement | Implementation |
|---|---|
| Spring Boot + Maven + MySQL + JPA/Hibernate + JSP/JSTL + CSS + JavaScript/jQuery + AJAX + MVC | `pom.xml`, `application.yml`, JSP webapp, static assets, controllers and repositories |
| Minimum 8 mapped entities | 15 JPA entities (one per SGA1 table) |
| Minimum 4 relationships including M:M | Customer-address, seller-product, category-product, customer-order, order-order item, order-payment, order-shipment, return-return item; cart-product and order-product are M:M via association entities `cart_items` and `order_items`; customer-product review is via `product_reviews` |
| Minimum 8 custom queries | Product catalogue/low-stock/top-selling; customer search; cart lock; order search/history; refund aggregation; return reservation aggregation; inventory history; monthly sales |
| Minimum 5 business rules | Unique customer email/SKU/seller/category; positive price/quantity; non-negative stock; one open cart/customer; address ownership; sufficient stock; return quantity cap; refund ceiling |
| Minimum 2 transactions | Checkout and return receipt/refund; restock is also transactional |
| Full CRUD for 4 entities | Products, categories, sellers and customers |
| Minimum 10 application pages | 20+ JSP view files excluding shared fragments/error |
| 3 client-validated forms | Product, category, seller, customer, restock, checkout and return forms use browser validation |
| 3 interactive features | AJAX add-to-cart, live stock check, confirm-delete, browser validity feedback |
| 2 AJAX features | Add-to-cart and stock lookup, both jQuery AJAX without page reload |
| 2 end-to-end workflows | Cart -> checkout -> stock ledger -> order history; return request -> approval -> receiving -> refund + stock restoration |
| Pagination and sorting on 2 pages | Product and customer listings support pagination and whitelisted sorting; orders also paginated |
| 3 report pages + dashboard | Low-stock, monthly sales, top products and overview dashboard |
| Automated tests | BusinessRulesTest, ProductRepositoryQueryTest, EndpointSmokeTest |
| 10 functional cases | `functional-test-cases.md` provides 12 cases, with actual execution pending local runtime |
