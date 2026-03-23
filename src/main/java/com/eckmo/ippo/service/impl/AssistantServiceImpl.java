package com.eckmo.ippo.service.impl;

import com.eckmo.ippo.domain.dto.response.AssistantResponse;
import com.eckmo.ippo.exception.ResourceNotFoundException;
import com.eckmo.ippo.repository.AssistantRepository;
import com.eckmo.ippo.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssistantServiceImpl implements AssistantService {

    private final AssistantRepository assistantRepository;

    @Override
    public List<AssistantResponse> findAll() {
        return assistantRepository.findAll().stream()
                .map(AssistantResponse::from)
                .toList();
    }

    @Override
    public AssistantResponse findById(Long id) {
        return assistantRepository.findById(id)
                .map(AssistantResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Assistant not found: " + id));
    }
}
