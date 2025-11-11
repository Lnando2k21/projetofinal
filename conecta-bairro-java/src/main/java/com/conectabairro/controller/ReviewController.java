package com.conectabairro.controller;

import com.conectabairro.dto.ReviewDto;
import com.conectabairro.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.conectabairro.model.User;
import com.conectabairro.repository.UserRepository;

/**
 * Controller para operações com avaliações
 */
@RestController
@RequestMapping("/reviews")
@Tag(name = "Avaliações", description = "Endpoints para gerenciar avaliações de serviços")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Criar avaliação", description = "Cria uma nova avaliação para um serviço")
    @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso",
            content = @Content(schema = @Schema(implementation = ReviewDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "409", description = "Conflito - serviço já foi avaliado")
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ReviewDto createdReview = reviewService.createReview(reviewDto, user.getId());
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter avaliação por ID", description = "Retorna as informações de uma avaliação específica")
    @ApiResponse(responseCode = "200", description = "Avaliação encontrada",
            content = @Content(schema = @Schema(implementation = ReviewDto.class)))
    @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto reviewDto = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewDto);
    }

    @GetMapping("/service/{serviceId}")
    @Operation(summary = "Listar avaliações de um serviço", description = "Retorna as avaliações de um serviço específico")
    @ApiResponse(responseCode = "200", description = "Avaliações encontradas")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public ResponseEntity<Page<ReviewDto>> getServiceReviews(
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDto> reviews = reviewService.getServiceReviews(serviceId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/my-reviews")
    @Operation(summary = "Listar minhas avaliações", description = "Retorna as avaliações do usuário logado")
    @ApiResponse(responseCode = "200", description = "Avaliações encontradas")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<Page<ReviewDto>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDto> reviews = reviewService.getMyReviews(user.getId(), pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar avaliação", description = "Atualiza uma avaliação existente")
    @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = ReviewDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ReviewDto updatedReview = reviewService.updateReview(id, reviewDto, user.getId());
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar avaliação", description = "Remove uma avaliação")
    @ApiResponse(responseCode = "204", description = "Avaliação deletada com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        reviewService.deleteReview(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
