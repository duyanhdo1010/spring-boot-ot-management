package com.spring.otmanagement.controller;

import com.spring.otmanagement.dto.OtRegistrationRequest;
import com.spring.otmanagement.dto.OtRegistrationResponse;
import com.spring.otmanagement.entity.OtRegistration;
import com.spring.otmanagement.service.OtRegistrationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otregistrations")
public class OtRegistrationController {
    private final OtRegistrationService otRegistrationService;

    public OtRegistrationController(OtRegistrationService otRegistrationService) {
        this.otRegistrationService = otRegistrationService;
    }

    @PostMapping
    public OtRegistrationResponse registerOt(@RequestBody @Valid OtRegistrationRequest otRegistrationRequest) {
        return this.otRegistrationService.registerOt(otRegistrationRequest);
    }
}
