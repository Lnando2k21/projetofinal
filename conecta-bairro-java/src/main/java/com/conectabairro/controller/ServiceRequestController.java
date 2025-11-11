package com.conectabairro.controller;

import com.conectabairro.dto.ServiceRequestDto;
import com.conectabairro.service.ServiceRequestService;
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
 * Controller para operações com requisições de serviço
 */
@RestController
@RequestMapping("/requests")
@Tag(name = "Requisições de Serviço", description = "Endpoints para gerenciar requisições de serviço")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Criar requisição de serviço", description = "Cria uma nova requisição de serviço")
    @ApiResponse(responseCode = "201", description = "Requisição criada com sucesso",
            content = @Content(schema = @Schema(implementation = ServiceRequestDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public ResponseEntity<ServiceRequestDto> createRequest(@Valid @RequestBody ServiceRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceRequestDto createdRequest = serviceRequestService.createRequest(
                requestDto.getServiceId(), user.getId(), requestDto);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter requisição por ID", description = "Retorna as informações de uma requisição específica")
    @ApiResponse(responseCode = "200", description = "Requisição encontrada",
            content = @Content(schema = @Schema(implementation = ServiceRequestDto.class)))
    @ApiResponse(responseCode = "404", description = "Requisição não encontrada")
    public ResponseEntity<ServiceRequestDto> getRequestById(@PathVariable Long id) {
        ServiceRequestDto requestDto = serviceRequestService.getRequestById(id);
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping("/my-requests")
    @Operation(summary = "Listar minhas requisições", description = "Retorna as requisições do cliente logado")
    @ApiResponse(responseCode = "200", description = "Requisições encontradas")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<Page<ServiceRequestDto>> getMyRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceRequestDto> requests = serviceRequestService.getMyRequests(user.getId(), pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/received")
    @Operation(summary = "Listar requisições recebidas", description = "Retorna as requisições recebidas pelo provedor logado")
    @ApiResponse(responseCode = "200", description = "Requisições encontradas")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<Page<ServiceRequestDto>> getReceivedRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceRequestDto> requests = serviceRequestService.getReceivedRequests(user.getId(), pageable);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/accept")
    @Operation(summary = "Aceitar requisição", description = "Aceita uma requisição de serviço")
    @ApiResponse(responseCode = "200", description = "Requisição aceita com sucesso",
            content = @Content(schema = @Schema(implementation = ServiceRequestDto.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Requisição não encontrada")
    public ResponseEntity<ServiceRequestDto> acceptRequest(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceRequestDto request = serviceRequestService.acceptRequest(id, user.getId());
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Rejeitar requisição", description = "Rejeita uma requisição de serviço")
    @ApiResponse(responseCode = "200", description = "Requisição rejeitada com sucesso",
            content = @Content(schema = @Schema(implementation = ServiceRequestDto.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Requisição não encontrada")
    public ResponseEntity<ServiceRequestDto> rejectRequest(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceRequestDto request = serviceRequestService.rejectRequest(id, user.getId());
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Completar requisição", description = "Marca uma requisição como completa")
    @ApiResponse(responseCode = "200", description = "Requisição marcada como completa",
            content = @Content(schema = @Schema(implementation = ServiceRequestDto.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Requisição não encontrada")
    public ResponseEntity<ServiceRequestDto> completeRequest(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceRequestDto request = serviceRequestService.completeRequest(id, user.getId());
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancelar requisição", description = "Cancela uma requisição de serviço")
    @ApiResponse(responseCode = "200", description = "Requisição cancelada com sucesso",
            content = @Content(schema = @Schema(implementation = ServiceRequestDto.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Requisição não encontrada")
    public ResponseEntity<ServiceRequestDto> cancelRequest(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceRequestDto request = serviceRequestService.cancelRequest(id, user.getId());
        return ResponseEntity.ok(request);
    }
}
