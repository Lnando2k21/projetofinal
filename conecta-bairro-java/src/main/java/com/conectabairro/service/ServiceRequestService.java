package com.conectabairro.service;

import com.conectabairro.dto.ServiceRequestDto;
import com.conectabairro.exception.ResourceNotFoundException;
import com.conectabairro.exception.UnauthorizedException;
import com.conectabairro.model.Service;
import com.conectabairro.model.ServiceRequest;
import com.conectabairro.model.User;
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
 * Serviço para gerenciar requisições de serviço
 */
@Service
@Slf4j
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ServiceRequestDto createRequest(Long serviceId, Long customerId, ServiceRequestDto requestDto) {
        log.info("Criando requisição de serviço para serviço: {} por cliente: {}", serviceId, customerId);

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if (customer.getUserType().equals(User.UserType.SERVICE_PROVIDER)) {
            throw new UnauthorizedException("Provedores de serviço não podem fazer requisições");
        }

        ServiceRequest request = ServiceRequest.builder()
                .service(service)
                .customer(customer)
                .scheduledDate(requestDto.getScheduledDate())
                .notes(requestDto.getNotes())
                .totalPrice(service.getPrice())
                .status(ServiceRequest.RequestStatus.PENDING)
                .build();

        ServiceRequest savedRequest = serviceRequestRepository.save(request);
        log.info("Requisição de serviço criada com sucesso: {}", savedRequest.getId());

        return mapToDto(savedRequest);
    }

    @Transactional(readOnly = true)
    public ServiceRequestDto getRequestById(Long id) {
        log.info("Buscando requisição com ID: {}", id);

        ServiceRequest request = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisição não encontrada"));

        return mapToDto(request);
    }

    @Transactional(readOnly = true)
    public Page<ServiceRequestDto> getMyRequests(Long customerId, Pageable pageable) {
        log.info("Buscando requisições do cliente: {}", customerId);

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return serviceRequestRepository.findByCustomer(customer, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<ServiceRequestDto> getReceivedRequests(Long providerId, Pageable pageable) {
        log.info("Buscando requisições recebidas pelo provider: {}", providerId);

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provedor não encontrado"));

        return serviceRequestRepository.findByService_Provider(provider, pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public ServiceRequestDto acceptRequest(Long id, Long providerId) {
        log.info("Aceitando requisição: {} por provider: {}", id, providerId);

        ServiceRequest request = serviceRequestRepository.findByIdAndService_Provider(id, 
                userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provedor não encontrado")))
                .orElseThrow(() -> new UnauthorizedException("Você não tem permissão para aceitar esta requisição"));

        request.setStatus(ServiceRequest.RequestStatus.ACCEPTED);
        ServiceRequest updatedRequest = serviceRequestRepository.save(request);
        log.info("Requisição aceita com sucesso: {}", id);

        return mapToDto(updatedRequest);
    }

    @Transactional
    public ServiceRequestDto rejectRequest(Long id, Long providerId) {
        log.info("Rejeitando requisição: {} por provider: {}", id, providerId);

        ServiceRequest request = serviceRequestRepository.findByIdAndService_Provider(id,
                userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provedor não encontrado")))
                .orElseThrow(() -> new UnauthorizedException("Você não tem permissão para rejeitar esta requisição"));

        request.setStatus(ServiceRequest.RequestStatus.REJECTED);
        ServiceRequest updatedRequest = serviceRequestRepository.save(request);
        log.info("Requisição rejeitada com sucesso: {}", id);

        return mapToDto(updatedRequest);
    }

    @Transactional
    public ServiceRequestDto completeRequest(Long id, Long providerId) {
        log.info("Marcando requisição como completa: {} por provider: {}", id, providerId);

        ServiceRequest request = serviceRequestRepository.findByIdAndService_Provider(id,
                userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provedor não encontrado")))
                .orElseThrow(() -> new UnauthorizedException("Você não tem permissão para completar esta requisição"));

        request.setStatus(ServiceRequest.RequestStatus.COMPLETED);
        ServiceRequest updatedRequest = serviceRequestRepository.save(request);
        log.info("Requisição marcada como completa: {}", id);

        return mapToDto(updatedRequest);
    }

    @Transactional
    public ServiceRequestDto cancelRequest(Long id, Long customerId) {
        log.info("Cancelando requisição: {} por cliente: {}", id, customerId);

        ServiceRequest request = serviceRequestRepository.findByIdAndCustomer(id,
                userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado")))
                .orElseThrow(() -> new UnauthorizedException("Você não tem permissão para cancelar esta requisição"));

        request.setStatus(ServiceRequest.RequestStatus.CANCELLED);
        ServiceRequest updatedRequest = serviceRequestRepository.save(request);
        log.info("Requisição cancelada com sucesso: {}", id);

        return mapToDto(updatedRequest);
    }

    private ServiceRequestDto mapToDto(ServiceRequest request) {
        return ServiceRequestDto.builder()
                .id(request.getId())
                .serviceId(request.getService().getId())
                .status(request.getStatus().toString())
                .scheduledDate(request.getScheduledDate())
                .notes(request.getNotes())
                .totalPrice(request.getTotalPrice())
                .customerId(request.getCustomer().getId())
                .customerName(request.getCustomer().getName())
                .providerId(request.getService().getProvider().getId())
                .providerName(request.getService().getProvider().getName())
                .serviceTitle(request.getService().getTitle())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
