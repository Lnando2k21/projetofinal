package com.conectabairro.repository;

import com.conectabairro.model.Service;
import com.conectabairro.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para operações com a entidade Service
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    List<Service> findByProvider(User provider);
    
    Page<Service> findByCategory(String category, Pageable pageable);
    
    Page<Service> findByLocation(String location, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Service> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.category = :category AND s.location = :location")
    Page<Service> findByCategoryAndLocation(@Param("category") String category, 
                                           @Param("location") String location, 
                                           Pageable pageable);
}
