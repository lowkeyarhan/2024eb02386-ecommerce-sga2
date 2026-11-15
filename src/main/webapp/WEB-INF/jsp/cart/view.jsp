<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="page-heading"><div><p class="eyebrow">Purchase flow</p><h1>Shopping cart</h1><p class="subheading">Add items from the catalogue, verify live stock and place a transactional order.</p></div><a class="button secondary" href="/products">Continue shopping</a></section>
<section class="panel">
  <form method="get" action="/cart" class="toolbar"><label for="customerId" style="margin:0">Demo customer</label><select id="customerId" name="customerId" style="max-width:280px"><c:forEach items="${customers}" var="customer"><option value="${customer.id}" <c:if test="${customer.id == selectedCustomerId}">selected</c:if>><c:out value="${customer.firstName}"/> <c:out value="${customer.lastName}"/> · #<c:out value="${customer.id}"/></option></c:forEach></select><button type="submit" class="secondary">Switch customer</button></form>
</section>
<div class="two-column">
  <section class="panel">
    <div class="page-heading"><div><p class="eyebrow">Items</p><h2 class="mb-0">Your selection</h2></div><span class="badge"><c:out value="${cart.itemCount}"/> units</span></div>
    <c:choose><c:when test="${not empty cart.items}">
      <div class="table-wrap"><table><thead><tr><th>Product</th><th>Price</th><th>Quantity</th><th>Line total</th><th></th></tr></thead><tbody>
      <c:forEach items="${cart.items}" var="item"><tr>
        <td><span class="table-title"><c:out value="${item.productName}"/></span><span class="table-meta"><c:out value="${item.sku}"/> · <c:out value="${item.stockQuantity}"/> in stock</span></td>
        <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencyCode="INR"/></td>
        <td><form method="post" action="/cart/items/${item.id}/update" class="inline-form"><input type="hidden" name="customerId" value="${selectedCustomerId}"><input name="quantity" type="number" min="1" max="${item.stockQuantity}" value="${item.quantity}" required><button class="small secondary" type="submit">Update</button></form></td>
        <td><strong><fmt:formatNumber value="${item.lineTotal}" type="currency" currencyCode="INR"/></strong></td>
        <td><form method="post" action="/cart/items/${item.id}/remove"><input type="hidden" name="customerId" value="${selectedCustomerId}"><button class="small danger" type="submit">Remove</button></form></td>
      </tr></c:forEach>
      </tbody></table></div>
    </c:when><c:otherwise><div class="empty"><strong>Your cart is empty.</strong>Add products to see them here.</div></c:otherwise></c:choose>
  </section>
  <section class="panel">
    <p class="eyebrow">Checkout</p><h2>Order summary</h2>
    <div class="detail-list"><span>Items</span><strong><c:out value="${cart.itemCount}"/></strong><span>Subtotal</span><strong><fmt:formatNumber value="${cart.subtotal}" type="currency" currencyCode="INR"/></strong><span>Shipping</span><strong>Calculated at checkout</strong><span>Tax</span><strong>18% on subtotal</strong></div>
    <hr style="border:0;border-top:1px solid var(--line);margin:18px 0">
    <form method="post" action="/orders/checkout" data-validate>
      <input type="hidden" name="customerId" value="${selectedCustomerId}">
      <div class="field"><label for="shippingAddressId">Shipping address</label><select id="shippingAddressId" name="shippingAddressId" required><option value="">Choose address</option><c:forEach items="${addresses}" var="address"><option value="${address.id}"><c:out value="${address.addressLine1}"/>, <c:out value="${address.city}"/>, <c:out value="${address.stateRegion}"/> <c:if test="${address.defaultAddress}">(default)</c:if></option></c:forEach></select><div class="help">The selected address must belong to the current customer.</div></div>
      <button type="submit" style="width:100%" <c:if test="${empty cart.items}">disabled</c:if>>Place order · demo COD</button>
    </form>
    <p class="help mt-16">Checkout runs in one database transaction: it locks stock rows, writes order snapshots, reduces inventory, adds a ledger entry and creates payment + shipment records. No real payment is taken.</p>
  </section>
</div>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
