<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Catalogue setup</p><h1><c:choose><c:when test="${not empty form.id}">Edit product</c:when><c:otherwise>New product</c:otherwise></c:choose></h1><p class="subheading">Product identity, pricing and inventory thresholds.</p></div><a class="button secondary" href="/products">Back to products</a></section>
<section class="panel">
<form method="post" action="/products/save" data-validate>
  <input type="hidden" name="id" value="${form.id}">
  <div class="form-grid">
    <div class="field"><label for="sku">SKU</label><input id="sku" name="sku" maxlength="40" required value="<c:out value='${form.sku}'/>" placeholder="e.g. MSE-002"></div>
    <div class="field"><label for="productName">Product name</label><input id="productName" name="productName" maxlength="140" required value="<c:out value='${form.productName}'/>" placeholder="Product name"></div>
    <div class="field"><label for="unitPrice">Unit price (INR)</label><input id="unitPrice" name="unitPrice" type="number" min="0.01" step="0.01" required value="${form.unitPrice}"></div>
    <div class="field"><label for="stockQuantity">Opening stock</label><input id="stockQuantity" name="stockQuantity" type="number" min="0" step="1" required value="${form.stockQuantity}"></div>
    <div class="field"><label for="reorderLevel">Reorder level</label><input id="reorderLevel" name="reorderLevel" type="number" min="0" step="1" required value="${form.reorderLevel}"></div>
    <div class="field"><label for="sellerId">Seller</label><select id="sellerId" name="sellerId" required><option value="">Choose seller</option><c:forEach items="${sellers}" var="seller"><option value="${seller.id}" <c:if test="${seller.id == form.sellerId}">selected</c:if>><c:out value="${seller.sellerName}"/></option></c:forEach></select></div>
    <div class="field"><label for="categoryId">Category</label><select id="categoryId" name="categoryId" required><option value="">Choose category</option><c:forEach items="${categories}" var="category"><option value="${category.id}" <c:if test="${category.id == form.categoryId}">selected</c:if>><c:out value="${category.categoryName}"/></option></c:forEach></select></div>
    <div class="field"><label for="active">Availability</label><select id="active" name="active"><option value="true" <c:if test="${form.active}">selected</c:if>>Active</option><option value="false" <c:if test="${not form.active}">selected</c:if>>Inactive</option></select></div>
    <div class="field full"><label for="description">Description</label><textarea id="description" name="description" rows="3" maxlength="4000"><c:out value="${form.description}"/></textarea></div>
  </div>
  <div class="action-row"><button type="submit">Save product</button><a class="button secondary" href="/products">Cancel</a></div>
</form>
</section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
