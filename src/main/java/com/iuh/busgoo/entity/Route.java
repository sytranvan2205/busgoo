package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.time.LocalTime;

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
@Table(name = "route")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Route extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String code;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private RegionDetail to;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private RegionDetail from;

    private Integer status;
    
    @Column(name = "transfer_time")
    private LocalTime transferTime;
    
    @ManyToOne
    @JoinColumn(name="price_detail_id")
    private PriceDetail priceDetail;
}
