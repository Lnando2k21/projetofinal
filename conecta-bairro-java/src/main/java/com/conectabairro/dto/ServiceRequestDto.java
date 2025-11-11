package com.conectabairro.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representação de requisição de serviço
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDto {
    private Long id;
    
    @NotNull(message = "ID do serviço é obrigatório")
    private Long serviceId;
    
    private String status;
    private LocalDateTime scheduledDate;
    private String notes;
    private Double totalPrice;
    private Long customerId;
    private String customerName;
    private Long providerId;
    private String providerName;
    private String serviceTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
