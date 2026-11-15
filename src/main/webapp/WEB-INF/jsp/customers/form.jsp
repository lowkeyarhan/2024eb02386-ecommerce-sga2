<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Customer records</p><h1>Customer details</h1></div><a class="button secondary" href="/customers">Back to customers</a></section>
<section class="panel"><form method="post" action="/customers/save" data-validate>
<input type="hidden" name="id" value="${form.id}">
<div class="form-grid">
<div class="field"><label for="firstName">First name</label><input id="firstName" name="firstName" maxlength="60" required value="<c:out value='${form.firstName}'/>"></div>
<div class="field"><label for="lastName">Last name</label><input id="lastName" name="lastName" maxlength="60" required value="<c:out value='${form.lastName}'/>"></div>
<div class="field"><label for="email">Email address</label><input id="email" name="email" type="email" maxlength="254" required value="<c:out value='${form.email}'/>"></div>
<div class="field"><label for="phone">Phone</label><input id="phone" name="phone" maxlength="30" value="<c:out value='${form.phone}'/>"></div>
</div>
<div class="action-row"><button type="submit">Save customer</button><a href="/customers" class="button secondary">Cancel</a></div>
</form></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
