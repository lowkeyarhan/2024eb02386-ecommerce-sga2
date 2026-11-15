package com.lowkeyarhan.ecommerce.orders.service;

import com.lowkeyarhan.ecommerce.common.error.BusinessRuleException;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import com.lowkeyarhan.ecommerce.common.validation.BusinessRules;
import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;
import com.lowkeyarhan.ecommerce.inventory.repository.InventoryMovementRepository;
import com.lowkeyarhan.ecommerce.orders.dto.ReturnRequestForm;
import com.lowkeyarhan.ecommerce.orders.model.*;
import com.lowkeyarhan.ecommerce.orders.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class ReturnService implements ReturnServicePort {
    private final OrderRepository orders; private final OrderItemRepository orderItems; private final ProductReturnRepository returns;
    private final ReturnItemRepository returnItems; private final ProductRepository products; private final PaymentRepository payments;
    private final InventoryMovementRepository movements; private final BusinessRules rules;

    @Transactional public ProductReturn request(ReturnRequestForm form) {
        PurchaseOrder order = orders.findByIdForUpdate(form.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        if (!List.of("DELIVERED", "RETURNED").contains(order.getStatus())) throw new BusinessRuleException("Only delivered orders can be returned.");
        OrderItem item = orderItems.findById(form.getOrderItemId()).orElseThrow(() -> new ResourceNotFoundException("Order item not found."));
        if (!item.getOrder().getId().equals(order.getId())) throw new BusinessRuleException("Selected item does not belong to this order.");
        rules.requirePositiveQuantity(form.getQuantity());
        long alreadyReserved = returnItems.sumReservedReturnQuantity(item.getId());
        if (alreadyReserved + form.getQuantity() > item.getQuantity()) throw new BusinessRuleException("Return quantity exceeds the unreturned purchased quantity.");
        ProductReturn request = new ProductReturn(); request.setOrder(order); request.setReason(form.getReason().trim()); request.setStatus("REQUESTED"); request = returns.save(request);
        ReturnItem line = new ReturnItem(); line.setProductReturn(request); line.setOrderItem(item); line.setQuantity(form.getQuantity()); returnItems.save(line);
        return request;
    }

    @Transactional public ProductReturn process(Long returnId, String action) {
        ProductReturn request = returns.findByIdForUpdate(returnId).orElseThrow(() -> new ResourceNotFoundException("Return request not found."));
        String normalized = action == null ? "" : action.trim().toUpperCase();
        if (normalized.equals("APPROVE") || normalized.equals("REJECT")) {
            if (!request.getStatus().equals("REQUESTED")) throw new BusinessRuleException("Only REQUESTED returns can be approved or rejected.");
            request.setStatus(normalized.equals("APPROVE") ? "APPROVED" : "REJECTED"); request.setProcessedAt(LocalDateTime.now()); return returns.save(request);
        }
        if (!normalized.equals("RECEIVE")) throw new BusinessRuleException("Action must be APPROVE, REJECT, or RECEIVE.");
        if (!request.getStatus().equals("APPROVED")) throw new BusinessRuleException("Return must be approved before receipt.");
        PurchaseOrder order = orders.findByIdForUpdate(request.getOrder().getId()).orElseThrow();
        if (!List.of("DELIVERED", "RETURNED").contains(order.getStatus())) throw new BusinessRuleException("Only delivered orders can be received as returns.");
        List<ReturnItem> lines = returnItems.findByProductReturn_Id(returnId);
        if (lines.isEmpty()) throw new BusinessRuleException("Return has no items.");
        Payment charge = payments.findFirstByOrder_IdAndPaymentKindAndStatus(order.getId(), "CHARGE", "COMPLETED")
                .orElseThrow(() -> new BusinessRuleException("A completed charge is required before refunding."));
        BigDecimal refundAmount = BigDecimal.ZERO;
        for (ReturnItem line : lines) refundAmount = refundAmount.add(line.getOrderItem().getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        BigDecimal prior = payments.sumCompletedRefunds(charge.getId());
        rules.requireRefundWithinCharge(refundAmount, prior, charge.getAmount());
        for (ReturnItem line : lines) {
            var product = products.findByIdForUpdate(line.getOrderItem().getProduct().getId()).orElseThrow();
            product.setStockQuantity(product.getStockQuantity() + line.getQuantity()); products.save(product);
            InventoryMovement movement = new InventoryMovement(); movement.setProduct(product); movement.setOrder(order); movement.setQuantityDelta(line.getQuantity());
            movement.setReason("RETURN"); movement.setNote("Received return #" + returnId); movements.save(movement);
        }
        Payment refund = new Payment(); refund.setOrder(order); refund.setPaymentKind("REFUND"); refund.setMethod(charge.getMethod());
        refund.setAmount(refundAmount); refund.setStatus("COMPLETED"); refund.setRelatedPayment(charge); refund.setReferenceCode("REFUND-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase()); payments.save(refund);
        request.setStatus("RECEIVED"); request.setProcessedAt(LocalDateTime.now()); returns.save(request);
        List<OrderItem> purchasedItems = orderItems.findByOrder_Id(order.getId());
        boolean fullyReturned = purchasedItems.stream().allMatch(oi -> returnItems.sumReceivedReturnQuantity(oi.getId()) >= oi.getQuantity());
        if (fullyReturned) { order.setStatus("RETURNED"); orders.save(order); }
        return request;
    }
    @Transactional(readOnly=true) public List<ProductReturn> findAll() { return returns.findAllByOrderByRequestedAtDesc(); }
}
