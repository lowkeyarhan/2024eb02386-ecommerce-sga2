<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">After-sales operations</p><h1>Request a return</h1><p class="subheading">Order #<c:out value="${order.id}"/> · return quantities cannot exceed the purchased quantity.</p></div><a class="button secondary" href="/orders/${order.id}">Back to order</a></section>
<section class="panel"><form method="post" action="/returns" data-validate>
<input type="hidden" name="orderId" value="${order.id}">
<div class="field"><label for="orderItemId">Order item</label><select id="orderItemId" name="orderItemId" required><option value="">Choose item</option><c:forEach items="${items}" var="item"><option value="${item.id}"><c:out value="${item.product.productName}"/> · <c:out value="${item.quantity}"/> purchased</option></c:forEach></select></div>
<div class="field"><label for="quantity">Quantity to return</label><input id="quantity" name="quantity" type="number" min="1" step="1" required></div>
<div class="field"><label for="reason">Reason for return</label><textarea id="reason" name="reason" rows="3" maxlength="300" required placeholder="Briefly describe the issue"></textarea></div>
<button type="submit">Submit return request</button>
</form></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
