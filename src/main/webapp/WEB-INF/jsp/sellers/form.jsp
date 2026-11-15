<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Marketplace partners</p><h1>Seller details</h1></div><a class="button secondary" href="/sellers">Back to sellers</a></section>
<section class="panel"><form method="post" action="/sellers/save" data-validate>
<input type="hidden" name="id" value="${form.id}">
<div class="field"><label for="sellerName">Seller name</label><input id="sellerName" name="sellerName" maxlength="120" required value="<c:out value='${form.sellerName}'/>" placeholder="Business name"></div>
<div class="field"><label for="contactEmail">Contact email</label><input id="contactEmail" name="contactEmail" type="email" maxlength="254" required value="<c:out value='${form.contactEmail}'/>" placeholder="team@example.com"></div>
<div class="field"><label for="status">Status</label><select id="status" name="status"><option value="ACTIVE" <c:if test="${form.status == 'ACTIVE'}">selected</c:if>>Active</option><option value="INACTIVE" <c:if test="${form.status == 'INACTIVE'}">selected</c:if>>Inactive</option></select></div>
<div class="action-row"><button type="submit">Save seller</button><a href="/sellers" class="button secondary">Cancel</a></div>
</form></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
