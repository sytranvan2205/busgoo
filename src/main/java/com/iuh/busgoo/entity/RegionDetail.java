package com.iuh.busgoo.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RegionDetail extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detail_code", length = 255)
    private String detailCode;

    @Column(name = "code_name", length = 255)
    private String codeName;

    private Integer status;

    @ManyToOne
    @JoinColumn(name = "region_parent_id")
    private RegionDetail regionParent;

    @ManyToOne
    @JoinColumn(name = "region_structure_id")
    private RegionStructure regionStructure;

    @Column(name = "full_name", length = 255) 
    private String fullName;
    
    @OneToOne(mappedBy = "regeionDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
    
}
