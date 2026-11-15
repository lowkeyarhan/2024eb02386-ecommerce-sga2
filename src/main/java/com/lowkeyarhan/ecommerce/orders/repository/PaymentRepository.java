package com.lowkeyarhan.ecommerce.orders.repository;

import com.lowkeyarhan.ecommerce.orders.model.Payment;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    java.util.List<Payment> findByOrder_IdOrderByCreatedAtDesc(Long orderId);
    Optional<Payment> findFirstByOrder_IdAndPaymentKindAndStatus(Long orderId, String paymentKind, String status);
    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.relatedPayment.id = :chargeId and p.paymentKind = 'REFUND' and p.status = 'COMPLETED'")
    BigDecimal sumCompletedRefunds(@Param("chargeId") Long chargeId);
}
