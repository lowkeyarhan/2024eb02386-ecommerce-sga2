<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Catalogue structure</p><h1>Categories</h1><p class="subheading">Organise products into a simple parent/child category hierarchy.</p></div><a class="button" href="/categories/new">+ Add category</a></section>
<section class="panel"><div class="table-wrap"><table><thead><tr><th>Category</th><th>Parent category</th><th>Actions</th></tr></thead><tbody>
<c:forEach items="${categories}" var="category"><tr><td><span class="table-title"><c:out value="${category.categoryName}"/></span><span class="table-meta">#<c:out value="${category.id}"/></span></td><td><c:out value="${category.parentCategory.categoryName}" default="Top level"/></td><td><div class="action-row"><a href="/categories/${category.id}/edit">Edit</a><form method="post" action="/categories/${category.id}/delete"><button class="small danger" data-confirm="Delete this category? Existing products may prevent deletion." type="submit">Delete</button></form></div></td></tr></c:forEach>
<c:if test="${empty categories}"><tr><td colspan="3" class="empty">No categories configured.</td></tr></c:if>
</tbody></table></div></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
