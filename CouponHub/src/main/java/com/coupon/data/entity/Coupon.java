package com.coupon.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcType(CharJdbcType.class)
    @Column(unique = true, nullable = false)
    private UUID uuid;

    @JdbcType(CharJdbcType.class)
    @Column(unique = true, nullable = false)
    private UUID companyUuid;

    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime startDate, endDate;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private URL imageUrl;
    @ElementCollection
    @CollectionTable(name = "customers",
            joinColumns ={@JoinColumn(name = "customer_id")})
    @Column(name = "coupon_id")
    private Set<UUID> customers;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationTimestamp;
    @UpdateTimestamp
    private Timestamp updateTimestamp;
}
