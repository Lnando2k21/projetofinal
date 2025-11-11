package com.conectabairro.repository;

import com.conectabairro.model.ServiceRequest;
import com.conectabairro.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações com a entidade ServiceRequest
 */
@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    
    Page<ServiceRequest> findByCustomer(User customer, Pageable pageable);
    
    Page<ServiceRequest> findByService_Provider(User provider, Pageable pageable);
    
    Optional<ServiceRequest> findByIdAndCustomer(Long id, User customer);
    
    Optional<ServiceRequest> findByIdAndService_Provider(Long id, User provider);
}
