package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PromotionDetail extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255)
    private String code;

    private BigDecimal discount;

    @Column(name = "max_discount")
    private BigDecimal maxDiscount;

    @Column(name = "discount_value", length = 255)
    private String discountValue;

    @ManyToOne
    @JoinColumn(name = "promotion_line_id")
    private PromotionLine promotionLine;
    
    @Column(name = "condition_apply", length = 255)
    private String conditionApply;
}
