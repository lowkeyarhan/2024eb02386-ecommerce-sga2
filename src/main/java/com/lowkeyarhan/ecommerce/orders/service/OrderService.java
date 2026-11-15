package com.lowkeyarhan.ecommerce.orders.service;

import com.lowkeyarhan.ecommerce.cart.model.Cart;
import com.lowkeyarhan.ecommerce.cart.model.CartItem;
import com.lowkeyarhan.ecommerce.cart.repository.CartRepository;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.common.error.BusinessRuleException;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import com.lowkeyarhan.ecommerce.common.validation.BusinessRules;
import com.lowkeyarhan.ecommerce.customer.model.CustomerAddress;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerAddressRepository;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerRepository;
import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;
import com.lowkeyarhan.ecommerce.inventory.repository.InventoryMovementRepository;
import com.lowkeyarhan.ecommerce.orders.model.*;
import com.lowkeyarhan.ecommerce.orders.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class OrderService implements OrderServicePort {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.18");
    private final CartRepository carts; private final CustomerRepository customers; private final CustomerAddressRepository addresses;
    private final ProductRepository products; private final OrderRepository orders; private final OrderItemRepository orderItems;
    private final PaymentRepository payments; private final ShipmentRepository shipments; private final InventoryMovementRepository movements;
    private final BusinessRules rules;

    @Transactional public PurchaseOrder checkout(Long customerId, Long addressId) {
        Cart cart = carts.lockOpenCart(customerId).orElseThrow(() -> new BusinessRuleException("There is no open cart for this customer."));
        if (cart.getItems().isEmpty()) throw new BusinessRuleException("Your cart is empty.");
        CustomerAddress address = addresses.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Shipping address not found."));
        if (!address.getCustomer().getId().equals(customerId)) throw new BusinessRuleException("The shipping address must belong to the selected customer.");
        List<CartItem> orderedItems = cart.getItems().stream().sorted(Comparator.comparing(i -> i.getProduct().getId())).toList();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : orderedItems) {
            Product locked = products.findByIdForUpdate(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
            if (!locked.isActive()) throw new BusinessRuleException(locked.getProductName() + " is no longer available.");
            rules.requireAvailableStock(item.getQuantity(), locked.getStockQuantity());
            subtotal = subtotal.add(locked.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal shipping = subtotal.compareTo(new BigDecimal("1999.00")) >= 0 ? BigDecimal.ZERO : new BigDecimal("49.00");
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        PurchaseOrder order = new PurchaseOrder(); order.setCustomer(customers.findById(customerId).orElseThrow()); order.setShippingAddress(address);
        order.setStatus("CONFIRMED"); order.setSubtotal(subtotal); order.setShippingFee(shipping); order.setTaxAmount(tax);
        order = orders.saveAndFlush(order);
        for (CartItem item : orderedItems) {
            Product product = products.findByIdForUpdate(item.getProduct().getId()).orElseThrow();
            rules.requireAvailableStock(item.getQuantity(), product.getStockQuantity());
            OrderItem line = new OrderItem(); line.setOrder(order); line.setProduct(product); line.setQuantity(item.getQuantity()); line.setUnitPrice(product.getUnitPrice());
            orderItems.save(line);
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity()); products.save(product);
            InventoryMovement movement = new InventoryMovement(); movement.setProduct(product); movement.setOrder(order); movement.setQuantityDelta(-item.getQuantity());
            movement.setReason("SALE"); movement.setNote("Stock deducted at checkout"); movements.save(movement);
        }
        Payment charge = new Payment(); charge.setOrder(order); charge.setPaymentKind("CHARGE"); charge.setMethod("COD");
        charge.setAmount(subtotal.add(shipping).add(tax)); charge.setStatus("COMPLETED"); charge.setReferenceCode("DEMO-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase()); payments.save(charge);
        Shipment shipment = new Shipment(); shipment.setOrder(order); shipment.setCarrier("Demo Express"); shipment.setTrackingNumber("ECOM-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        shipment.setStatus("PREPARING"); shipments.save(shipment);
        cart.setStatus("CONVERTED"); carts.save(cart);
        return orders.findById(order.getId()).orElse(order);
    }
    @Transactional(readOnly=true) public Page<PurchaseOrder> search(String status, String keyword, Pageable pageable) { return orders.searchOrders(blankToNull(status), blankToNull(keyword), pageable); }
    @Transactional(readOnly=true) public Page<PurchaseOrder> history(Long customerId, Pageable pageable) { return orders.findByCustomer_IdOrderByOrderDateDesc(customerId, pageable); }
    @Transactional(readOnly=true) public PurchaseOrder get(Long id) { return orders.findDetailedById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id)); }
    @Transactional(readOnly=true) public List<OrderItem> items(Long orderId) { return orderItems.findDetailedItems(orderId); }
    @Transactional(readOnly=true) public List<Payment> payments(Long orderId) { return payments.findByOrder_IdOrderByCreatedAtDesc(orderId); }
    @Transactional(readOnly=true) public java.util.Optional<Shipment> shipment(Long orderId) { return shipments.findByOrder_Id(orderId); }
    @Transactional(readOnly=true) public List<PurchaseOrder> recent() { return orders.findTop8ByOrderByOrderDateDesc(); }
    private String blankToNull(String v) { return v == null || v.isBlank() ? null : v.trim(); }
}
