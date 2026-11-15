package com.lowkeyarhan.ecommerce.reports.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {
    private final JdbcTemplate jdbc;
    public ReportRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public List<MonthlySalesRow> monthlySales() {
        return jdbc.query("select date_format(o.order_date, '%Y-%m') as month_label, coalesce(sum(case when p.payment_kind='CHARGE' then p.amount else -p.amount end),0) as net_sales from orders o join payments p on p.order_id=o.order_id where p.status='COMPLETED' group by date_format(o.order_date, '%Y-%m') order by month_label desc limit 12",
            (rs, n) -> new MonthlySalesRow(rs.getString("month_label"), rs.getBigDecimal("net_sales")));
    }
    public record MonthlySalesRow(String month, BigDecimal netSales) {}
}
