package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "promotion_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PromotionLine extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255)
    private String code;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "line_name", length = 255)
    private String lineName;

    @Column(name = "promotion_type", length = 255)
    private String promotionType;
    
    @Column(name = "condition_apply", length = 255)
    private String conditionApply;
    
    @Column
    private Integer status;
    
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
    
//    @OneToMany(mappedBy = "promotionLine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PromotionDetail> promotionDetails;
}
