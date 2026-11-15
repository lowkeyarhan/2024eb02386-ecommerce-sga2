<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Catalogue structure</p><h1>Category details</h1></div><a class="button secondary" href="/categories">Back to categories</a></section>
<section class="panel"><form method="post" action="/categories/save" data-validate>
<input type="hidden" name="id" value="${form.id}">
<div class="field"><label for="categoryName">Category name</label><input id="categoryName" name="categoryName" maxlength="80" required value="<c:out value='${form.categoryName}'/>" placeholder="e.g. Gaming"></div>
<div class="field"><label for="parentCategoryId">Parent category</label><select id="parentCategoryId" name="parentCategoryId"><option value="">No parent</option><c:forEach items="${allCategories}" var="category"><c:if test="${category.id != form.id}"><option value="${category.id}" <c:if test="${category.id == form.parentCategoryId}">selected</c:if>><c:out value="${category.categoryName}"/></option></c:if></c:forEach></select></div>
<div class="action-row"><button type="submit">Save category</button><a class="button secondary" href="/categories">Cancel</a></div>
</form></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
