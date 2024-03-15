package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "time_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TimeTable extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255)
    private String code;

    @Column(name = "started_time")
    private LocalDateTime startedTime;

    private Integer status;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
    
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
