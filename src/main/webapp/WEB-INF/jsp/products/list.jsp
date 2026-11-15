<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Catalogue</p><h1>Products</h1><p class="subheading">Search the catalogue, inspect live stock and add products to the selected customer's cart.</p></div><a class="button" href="/products/new">+ Add product</a></section>
<section class="panel">
  <form class="toolbar" method="get" action="/products">
    <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Search name or SKU">
    <select name="categoryId"><option value="">All categories</option><c:forEach items="${categories}" var="category"><option value="${category.id}" <c:if test="${category.id == categoryId}">selected</c:if>><c:out value="${category.categoryName}"/></option></c:forEach></select>
    <select name="sellerId"><option value="">All sellers</option><c:forEach items="${sellers}" var="seller"><option value="${seller.id}" <c:if test="${seller.id == sellerId}">selected</c:if>><c:out value="${seller.sellerName}"/></option></c:forEach></select>
    <select name="sort"><option value="productName">Name</option><option value="unitPrice" <c:if test="${sort == 'unitPrice'}">selected</c:if>>Price</option><option value="stockQuantity" <c:if test="${sort == 'stockQuantity'}">selected</c:if>>Stock</option></select>
    <select name="direction"><option value="asc">Ascending</option><option value="desc" <c:if test="${direction == 'desc'}">selected</c:if>>Descending</option></select>
    <select name="customerId"><c:forEach items="${customers}" var="customer"><option value="${customer.id}" <c:if test="${customer.id == selectedCustomerId}">selected</c:if>><c:out value="${customer.firstName}"/> <c:out value="${customer.lastName}"/></option></c:forEach></select>
    <button type="submit">Apply filters</button>
    <a class="button secondary" href="/products">Reset</a>
  </form>
</section>
<div class="product-grid">
  <c:forEach items="${products}" var="product">
    <article class="product-card">
      <div class="sku"><c:out value="${product.sku}"/> · <c:out value="${product.category.categoryName}"/></div>
      <h3><c:out value="${product.productName}"/></h3>
      <p><c:out value="${product.description}"/></p>
      <div class="muted" style="font-size:11px;margin-bottom:10px"><c:out value="${product.seller.sellerName}"/></div>
      <div class="product-foot"><div><div class="price"><fmt:formatNumber value="${product.unitPrice}" type="currency" currencyCode="INR"/></div><span class="table-meta"><c:out value="${product.stockQuantity}"/> in stock</span></div>
        <div class="action-row"><button class="small secondary" type="button" data-check-stock data-product-id="${product.id}">Stock</button><button class="small" type="button" data-add-cart data-product-id="${product.id}" data-customer-id="${selectedCustomerId}" <c:if test="${!product.active || product.stockQuantity == 0}">disabled</c:if>>Add</button></div>
      </div>
      <div class="status-line" id="stock-${product.id}" aria-live="polite"></div>
      <div class="action-row mt-16"><a href="/products/${product.id}/edit">Edit</a><form method="post" action="/products/${product.id}/delete"><button class="small danger" type="submit" data-confirm="Delete this product?">Delete</button></form></div>
    </article>
  </c:forEach>
  <c:if test="${empty products}"><div class="panel empty" style="grid-column:1/-1"><strong>No products match these filters.</strong>Try another keyword or clear the filters.</div></c:if>
</div>
<div class="pagination"><span><c:out value="${productPage.totalElements}"/> products · page <c:out value="${productPage.number + 1}"/> of <c:out value="${productPage.totalPages}"/></span><div class="actions"><c:if test="${productPage.hasPrevious()}"><a class="button secondary small" href="/products?q=${q}&categoryId=${categoryId}&sellerId=${sellerId}&sort=${sort}&direction=${direction}&customerId=${selectedCustomerId}&page=${productPage.number - 1}">Previous</a></c:if><c:if test="${productPage.hasNext()}"><a class="button secondary small" href="/products?q=${q}&categoryId=${categoryId}&sellerId=${sellerId}&sort=${sort}&direction=${direction}&customerId=${selectedCustomerId}&page=${productPage.number + 1}">Next</a></c:if></div></div>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
