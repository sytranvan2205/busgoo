package com.iuh.busgoo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@CreatedDate
	private LocalDate createdDate;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedDate
	private LocalDate lastModifiedDate;
	
	@LastModifiedBy
	private String lastModifiedBy;
	
	@PrePersist
    protected void onCreate() {
		createdDate = LocalDate.now();
		createdBy = getCurrentUser();
    }
	
    @PreUpdate
    protected void onUpdate() {
    	lastModifiedDate = LocalDate.now();
    	lastModifiedBy = getCurrentUser();
    }
    
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUser = (String) authentication.getPrincipal();
            return currentUser;
        }
        return "anonymous"; // Trả về một giá trị mặc định nếu không có ai được xác thực
    }
}
