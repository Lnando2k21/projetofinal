package com.conectabairro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representação de serviço
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    private String title;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String description;
    
    @NotBlank(message = "Categoria é obrigatória")
    private String category;
    
    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço não pode ser negativo")
    private Double price;
    
    @NotBlank(message = "Localização é obrigatória")
    private String location;
    
    private String imageUrl;
    private Double rating;
    private Integer totalReviews;
    private Boolean isActive;
    private Long providerId;
    private String providerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
