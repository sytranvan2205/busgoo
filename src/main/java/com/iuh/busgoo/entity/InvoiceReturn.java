package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "invoice_return")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class InvoiceReturn extends AbstractEntity implements Serializable{
	
	private static final long serialVersionUID = -2856149540055183433L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255)
    private String code;

    @Column(name="station_id")
    private Long stationId;

    @Column(name = "status")
    private Integer status;
    
    @Column(name = "route_id")
    private Long routeId;
    
    @Column(name = "time_booking")
    private Date timeBooking;
    
    @Column(name = "reason", length = 500)
    private String reason;
    
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    
    @OneToMany(mappedBy = "invoiceReturn", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceReturnDetail> invoiceReturnDetails;
}
