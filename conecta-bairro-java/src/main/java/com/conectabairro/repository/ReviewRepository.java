package com.conectabairro.repository;

import com.conectabairro.model.Review;
import com.conectabairro.model.Service;
import com.conectabairro.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações com a entidade Review
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByService(Service service, Pageable pageable);
    
    Page<Review> findByReviewer(User reviewer, Pageable pageable);
    
    Optional<Review> findByRequest_Id(Long requestId);
    
    boolean existsByRequest_Id(Long requestId);
}
