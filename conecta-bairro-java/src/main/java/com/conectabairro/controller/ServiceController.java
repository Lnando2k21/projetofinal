package com.conectabairro.controller;

import com.conectabairro.dto.ServiceDto;
import com.conectabairro.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.conectabairro.model.User;
import com.conectabairro.repository.UserRepository;

/**
 * Controller para operações com serviços
 */
@RestController
@RequestMapping("/services")
@Tag(name = "Serviços", description = "Endpoints para gerenciar serviços")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Criar novo serviço", description = "Cria um novo serviço (apenas para provedores)")
    @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso",
            content = @Content(schema = @Schema(implementation = ServiceDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido - não é um provedor")
    public ResponseEntity<ServiceDto> createService(@Valid @RequestBody ServiceDto serviceDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceDto createdService = serviceService.createService(serviceDto, user.getId());
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter serviço por ID", description = "Retorna as informações de um serviço específico")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado",
            content = @Content(schema = @Schema(implementation = ServiceDto.class)))
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        ServiceDto serviceDto = serviceService.getServiceById(id);
        return ResponseEntity.ok(serviceDto);
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços", description = "Retorna uma página de serviços")
    @ApiResponse(responseCode = "200", description = "Serviços encontrados")
    public ResponseEntity<Page<ServiceDto>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceDto> services = serviceService.getAllServices(pageable);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar serviços", description = "Busca serviços por palavra-chave")
    @ApiResponse(responseCode = "200", description = "Serviços encontrados")
    public ResponseEntity<Page<ServiceDto>> searchServices(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceDto> services = serviceService.searchServices(keyword, pageable);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Listar serviços por categoria", description = "Retorna serviços de uma categoria específica")
    @ApiResponse(responseCode = "200", description = "Serviços encontrados")
    public ResponseEntity<Page<ServiceDto>> getServicesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceDto> services = serviceService.getServicesByCategory(category, pageable);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Listar serviços por localização", description = "Retorna serviços de uma localização específica")
    @ApiResponse(responseCode = "200", description = "Serviços encontrados")
    public ResponseEntity<Page<ServiceDto>> getServicesByLocation(
            @PathVariable String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceDto> services = serviceService.getServicesByLocation(location, pageable);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Listar serviços de um provedor", description = "Retorna todos os serviços de um provedor específico")
    @ApiResponse(responseCode = "200", description = "Serviços encontrados")
    @ApiResponse(responseCode = "404", description = "Provedor não encontrado")
    public ResponseEntity<?> getServicesByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(serviceService.getServicesByProvider(providerId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar serviço", description = "Atualiza as informações de um serviço")
    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ServiceDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public ResponseEntity<ServiceDto> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceDto serviceDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        ServiceDto updatedService = serviceService.updateService(id, serviceDto, user.getId());
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar serviço", description = "Remove um serviço")
    @ApiResponse(responseCode = "204", description = "Serviço deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Acesso proibido")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        
        serviceService.deleteService(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
