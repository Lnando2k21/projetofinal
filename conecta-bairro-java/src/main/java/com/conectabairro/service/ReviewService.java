package com.conectabairro.service;

import com.conectabairro.dto.ReviewDto;
import com.conectabairro.exception.ResourceNotFoundException;
import com.conectabairro.exception.UnauthorizedException;
import com.conectabairro.model.Review;
import com.conectabairro.model.Service;
import com.conectabairro.model.ServiceRequest;
import com.conectabairro.model.User;
import com.conectabairro.repository.ReviewRepository;
import com.conectabairro.repository.ServiceRepository;
import com.conectabairro.repository.ServiceRequestRepository;
import com.conectabairro.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço para gerenciar avaliações
 */
@Service
@Slf4j
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto, Long reviewerId) {
        log.info("Criando review para requisição: {} por reviewer: {}", reviewDto.getRequestId(), reviewerId);

        ServiceRequest request = serviceRequestRepository.findById(reviewDto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Requisição não encontrada"));

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer não encontrado"));

        if (!request.getCustomer().getId().equals(reviewerId)) {
            throw new UnauthorizedException("Apenas o cliente pode avaliar este serviço");
        }

        if (!request.getStatus().equals(ServiceRequest.RequestStatus.COMPLETED)) {
            throw new IllegalStateException("Apenas requisições completas podem ser avaliadas");
        }

        if (reviewRepository.existsByRequest_Id(reviewDto.getRequestId())) {
            throw new IllegalStateException("Este serviço já foi avaliado");
        }

        Service service = request.getService();

        Review review = Review.builder()
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .reviewer(reviewer)
                .service(service)
                .request(request)
                .build();

        Review savedReview = reviewRepository.save(review);
        request.setReview(savedReview);
        serviceRequestRepository.save(request);

        updateServiceRating(service);

        log.info("Review criada com sucesso: {}", savedReview.getId());

        return mapToDto(savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long id) {
        log.info("Buscando review com ID: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review não encontrada"));

        return mapToDto(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getServiceReviews(Long serviceId, Pageable pageable) {
        log.info("Buscando reviews do serviço: {}", serviceId);

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        return reviewRepository.findByService(service, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getMyReviews(Long reviewerId, Pageable pageable) {
        log.info("Buscando reviews do usuário: {}", reviewerId);

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer não encontrado"));

        return reviewRepository.findByReviewer(reviewer, pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public ReviewDto updateReview(Long id, ReviewDto reviewDto, Long reviewerId) {
        log.info("Atualizando review: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review não encontrada"));

        if (!review.getReviewer().getId().equals(reviewerId)) {
            throw new UnauthorizedException("Você não tem permissão para atualizar esta review");
        }

        if (reviewDto.getRating() != null) {
            review.setRating(reviewDto.getRating());
        }
        if (reviewDto.getComment() != null) {
            review.setComment(reviewDto.getComment());
        }

        Review updatedReview = reviewRepository.save(review);

        updateServiceRating(review.getService());

        log.info("Review atualizada com sucesso: {}", id);

        return mapToDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long id, Long reviewerId) {
        log.info("Deletando review: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review não encontrada"));

        if (!review.getReviewer().getId().equals(reviewerId)) {
            throw new UnauthorizedException("Você não tem permissão para deletar esta review");
        }

        Service service = review.getService();
        reviewRepository.delete(review);

        updateServiceRating(service);

        log.info("Review deletada com sucesso: {}", id);
    }

    private void updateServiceRating(Service service) {
        Page<Review> reviews = reviewRepository.findByService(service, org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE));
        
        if (reviews.hasContent()) {
            double averageRating = reviews.getContent()
                    .stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);

            service.setRating(averageRating);
            service.setTotalReviews(reviews.getContent().size());
            serviceRepository.save(service);

            // Atualizar rating do provider
            User provider = service.getProvider();
            Page<Review> providerReviews = reviewRepository.findByReviewer(provider, 
                    org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE));
            
            if (providerReviews.hasContent()) {
                double providerAverageRating = providerReviews.getContent()
                        .stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);

                provider.setRating(providerAverageRating);
                provider.setTotalReviews(providerReviews.getContent().size());
                userRepository.save(provider);
            }
        }
    }

    private ReviewDto mapToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .requestId(review.getRequest().getId())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getName())
                .serviceId(review.getService().getId())
                .serviceTitle(review.getService().getTitle())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
