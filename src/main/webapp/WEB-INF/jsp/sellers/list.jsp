<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Marketplace partners</p><h1>Sellers</h1><p class="subheading">Manage the sellers responsible for the product catalogue.</p></div><a class="button" href="/sellers/new">+ Add seller</a></section>
<section class="panel"><div class="table-wrap"><table><thead><tr><th>Seller</th><th>Contact email</th><th>Status</th><th>Actions</th></tr></thead><tbody>
<c:forEach items="${sellers}" var="seller"><tr><td class="table-title"><c:out value="${seller.sellerName}"/></td><td><c:out value="${seller.contactEmail}"/></td><td><span class="badge success"><c:out value="${seller.status}"/></span></td><td><div class="action-row"><a href="/sellers/${seller.id}/edit">Edit</a><form method="post" action="/sellers/${seller.id}/delete"><button type="submit" class="small danger" data-confirm="Delete this seller? Products may prevent deletion.">Delete</button></form></div></td></tr></c:forEach>
<c:if test="${empty sellers}"><tr><td colspan="4" class="empty">No sellers found.</td></tr></c:if>
</tbody></table></div></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
