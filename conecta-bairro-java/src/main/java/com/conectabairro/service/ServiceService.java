package com.conectabairro.service;

import com.conectabairro.dto.ServiceDto;
import com.conectabairro.exception.ResourceNotFoundException;
import com.conectabairro.exception.UnauthorizedException;
import com.conectabairro.model.Service;
import com.conectabairro.model.User;
import com.conectabairro.repository.ServiceRepository;
import com.conectabairro.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar serviços
 */
@Service
@Slf4j
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ServiceDto createService(ServiceDto serviceDto, Long providerId) {
        log.info("Criando novo serviço para provider: {}", providerId);

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provedor não encontrado"));

        if (!provider.getUserType().equals(User.UserType.SERVICE_PROVIDER)) {
            throw new UnauthorizedException("Apenas provedores de serviço podem criar serviços");
        }

        Service service = Service.builder()
                .title(serviceDto.getTitle())
                .description(serviceDto.getDescription())
                .category(serviceDto.getCategory())
                .price(serviceDto.getPrice())
                .location(serviceDto.getLocation())
                .imageUrl(serviceDto.getImageUrl())
                .provider(provider)
                .build();

        Service savedService = serviceRepository.save(service);
        log.info("Serviço criado com sucesso: {}", savedService.getId());

        return mapToDto(savedService);
    }

    @Transactional(readOnly = true)
    public ServiceDto getServiceById(Long id) {
        log.info("Buscando serviço com ID: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));

        return mapToDto(service);
    }

    @Transactional(readOnly = true)
    public Page<ServiceDto> getAllServices(Pageable pageable) {
        log.info("Listando todos os serviços");
        
        return serviceRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<ServiceDto> searchServices(String keyword, Pageable pageable) {
        log.info("Buscando serviços com keyword: {}", keyword);

        return serviceRepository.searchByKeyword(keyword, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<ServiceDto> getServicesByCategory(String category, Pageable pageable) {
        log.info("Buscando serviços por categoria: {}", category);

        return serviceRepository.findByCategory(category, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<ServiceDto> getServicesByLocation(String location, Pageable pageable) {
        log.info("Buscando serviços por localização: {}", location);

        return serviceRepository.findByLocation(location, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<ServiceDto> getServicesByProvider(Long providerId) {
        log.info("Buscando serviços do provider: {}", providerId);

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provedor não encontrado"));

        return serviceRepository.findByProvider(provider)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceDto updateService(Long id, ServiceDto serviceDto, Long providerId) {
        log.info("Atualizando serviço: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        if (!service.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedException("Você não tem permissão para atualizar este serviço");
        }

        if (serviceDto.getTitle() != null) {
            service.setTitle(serviceDto.getTitle());
        }
        if (serviceDto.getDescription() != null) {
            service.setDescription(serviceDto.getDescription());
        }
        if (serviceDto.getCategory() != null) {
            service.setCategory(serviceDto.getCategory());
        }
        if (serviceDto.getPrice() != null) {
            service.setPrice(serviceDto.getPrice());
        }
        if (serviceDto.getLocation() != null) {
            service.setLocation(serviceDto.getLocation());
        }
        if (serviceDto.getImageUrl() != null) {
            service.setImageUrl(serviceDto.getImageUrl());
        }

        Service updatedService = serviceRepository.save(service);
        log.info("Serviço atualizado com sucesso: {}", id);

        return mapToDto(updatedService);
    }

    @Transactional
    public void deleteService(Long id, Long providerId) {
        log.info("Deletando serviço: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        if (!service.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedException("Você não tem permissão para deletar este serviço");
        }

        serviceRepository.delete(service);
        log.info("Serviço deletado com sucesso: {}", id);
    }

    private ServiceDto mapToDto(Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .title(service.getTitle())
                .description(service.getDescription())
                .category(service.getCategory())
                .price(service.getPrice())
                .location(service.getLocation())
                .imageUrl(service.getImageUrl())
                .rating(service.getRating())
                .totalReviews(service.getTotalReviews())
                .isActive(service.getIsActive())
                .providerId(service.getProvider().getId())
                .providerName(service.getProvider().getName())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }
}
