package com.conectabairro.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representação de avaliação
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    
    @NotNull(message = "Avaliação é obrigatória")
    @Min(value = 1, message = "Avaliação mínima é 1")
    @Max(value = 5, message = "Avaliação máxima é 5")
    private Integer rating;
    
    @NotBlank(message = "Comentário é obrigatório")
    @Size(min = 10, max = 1000, message = "Comentário deve ter entre 10 e 1000 caracteres")
    private String comment;
    
    @NotNull(message = "ID da requisição é obrigatório")
    private Long requestId;
    
    private Long reviewerId;
    private String reviewerName;
    private Long serviceId;
    private String serviceTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
