# URL mapping reference

| HTTP | URL | Purpose |
|---|---|---|
| GET | `/` | Dashboard counts and recent orders |
| GET | `/products` | Searchable/sortable product list |
| GET | `/products/new` | Create product form |
| GET | `/products/{id}/edit` | Edit product form |
| POST | `/products/save` | Create/update product |
| POST | `/products/{id}/delete` | Delete product |
| GET | `/api/products/{id}/stock` | AJAX stock lookup |
| GET | `/categories` | Category list |
| GET | `/categories/new` | Category form |
| GET | `/categories/{id}/edit` | Edit category form |
| POST | `/categories/save` | Create/update category |
| POST | `/categories/{id}/delete` | Delete category |
| GET | `/sellers` | Seller list |
| GET | `/sellers/new` | Seller form |
| GET | `/sellers/{id}/edit` | Edit seller form |
| POST | `/sellers/save` | Create/update seller |
| POST | `/sellers/{id}/delete` | Delete seller |
| GET | `/customers` | Searchable, sortable, paginated customers |
| GET | `/customers/new` | Customer form |
| GET | `/customers/{id}/edit` | Edit customer form |
| POST | `/customers/save` | Create/update customer |
| POST | `/customers/{id}/delete` | Delete customer |
| GET | `/cart` | Cart and checkout summary |
| POST | `/api/cart/items` | AJAX add-to-cart |
| POST | `/cart/items/{itemId}/update` | Update cart quantity |
| POST | `/cart/items/{itemId}/remove` | Remove cart item |
| GET | `/orders` | Search and paginate orders |
| GET | `/orders/history` | Customer order history |
| GET | `/orders/{id}` | Order detail, items, payment and shipment |
| POST | `/orders/checkout` | Transactional checkout |
| GET | `/returns` | Return queue |
| GET | `/returns/new?orderId={id}` | Return request form |
| POST | `/returns` | Create return request |
| POST | `/returns/{id}/action` | Approve, reject or receive return |
| GET | `/inventory` | Stock alerts and recent movements |
| POST | `/inventory/restock` | Transactional restock |
| GET | `/reports` | Reports landing page |
| GET | `/reports/low-stock` | Low-stock report |
| GET | `/reports/monthly-sales` | Monthly net-sales report |
| GET | `/reports/top-products` | Top-selling products report |
| GET | `/api/health` | Application health endpoint |
