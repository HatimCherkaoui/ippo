package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.response.AssistantResponse;

import java.util.List;

public interface AssistantService {
    List<AssistantResponse> findAll();
    AssistantResponse findById(Long id);
}
