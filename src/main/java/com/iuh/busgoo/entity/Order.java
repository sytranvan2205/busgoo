package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Order extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = -7998603172913338136L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255)
    private String code;

    @Column(name = "is_pay")
    private Integer isPay;
    
    @Column
    private Integer status;
    
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "route_id") private Route route;
	 */
    
    @ManyToOne
    @JoinColumn(name = "promotion_detail_id")
    private PromotionDetail promotionDetail;
    
    @Column(name="pick_up_point")
    private Long pickUpPoint;
    
	/*
	 * @Column(name="drop_off_point") private Long dropOffPoint;
	 */
    @Column(name = "total_tiket_price")
    private Double totalTiketPrice;
    
    @Column(name ="total_discount")
    private Double totalDiscount;
    
    @Column(name="total")
    private Double total;
    
    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;
    
	/*
	 * @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch =
	 * FetchType.EAGER) private List<OrderDetail> orderDetails;
	 */
}
