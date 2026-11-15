<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading">
  <div><p class="eyebrow">Operations overview</p><h1>Good work starts with<br>clear numbers.</h1><p class="subheading">A live snapshot of the catalogue, customers, orders and inventory for the demo workspace.</p></div>
  <div class="action-row"><a class="button secondary" href="/reports">View reports</a><a class="button" href="/products/new">Add product</a></div>
</section>
<div class="metric-grid">
  <div class="metric-card"><div class="metric-label">Products</div><div class="metric-value"><c:out value="${dashboard.products}"/></div><div class="metric-foot">Across the catalogue</div></div>
  <div class="metric-card"><div class="metric-label">Customers</div><div class="metric-value"><c:out value="${dashboard.customers}"/></div><div class="metric-foot">Customer records</div></div>
  <div class="metric-card"><div class="metric-label">Active orders</div><div class="metric-value"><c:out value="${dashboard.activeOrders}"/></div><div class="metric-foot">Confirmed, processing or shipped</div></div>
  <div class="metric-card"><div class="metric-label">Low-stock products</div><div class="metric-value"><c:out value="${dashboard.lowStockProducts}"/></div><div class="metric-foot">At or below reorder threshold</div></div>
</div>
<div class="grid-2">
  <section class="panel">
    <div class="page-heading"><div><p class="eyebrow">Recent activity</p><h2 class="mb-0">Latest orders</h2></div><a href="/orders">All orders →</a></div>
    <div class="table-wrap"><table><thead><tr><th>Order</th><th>Customer</th><th>Date</th><th>Status</th></tr></thead><tbody>
      <c:forEach items="${dashboard.recentOrders}" var="order"><tr><td><a class="table-title" href="/orders/${order.id}">#<c:out value="${order.id}"/></a></td><td><c:out value="${order.customer.firstName}"/> <c:out value="${order.customer.lastName}"/></td><td><c:out value="${order.orderDate}"/></td><td><span class="badge success"><c:out value="${order.status}"/></span></td></tr></c:forEach>
      <c:if test="${empty dashboard.recentOrders}"><tr><td colspan="4" class="empty">No orders yet.</td></tr></c:if>
    </tbody></table></div>
  </section>
  <section class="panel">
    <p class="eyebrow">Quick access</p><h2>Workspaces</h2>
    <div class="grid-2">
      <a class="report-card" href="/cart"><div class="icon">01</div><h3>Cart & checkout</h3><p>Test AJAX cart updates and the transactional checkout flow.</p></a>
      <a class="report-card" href="/inventory"><div class="icon">02</div><h3>Stock control</h3><p>Review low-stock alerts and record replenishment.</p></a>
      <a class="report-card" href="/returns"><div class="icon">03</div><h3>Returns desk</h3><p>Review requests and process stock restoration and refunds.</p></a>
      <a class="report-card" href="/reports"><div class="icon">04</div><h3>Business reports</h3><p>Explore sales, inventory and product performance.</p></a>
    </div>
  </section>
</div>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
