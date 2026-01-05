package com.spring.otmanagement.controller;

import com.spring.otmanagement.dto.OtRegistrationRequest;
import com.spring.otmanagement.dto.OtRegistrationResponse;
import com.spring.otmanagement.dto.OtStatusUpdate;
import com.spring.otmanagement.service.OtRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<OtRegistrationResponse>> getAllOtRegistrations() {
        return ResponseEntity.status(200).body(this.otRegistrationService.getAllOtRegistrations());
    }

    @GetMapping("/{id}")
    public OtRegistrationResponse getOtRegistration(@PathVariable Long id) {
        return this.otRegistrationService.getOtRegistration(id);
    }

    @PatchMapping("/{id}/status")
    public OtRegistrationResponse updateOtStatus(@PathVariable Long id, @RequestBody OtStatusUpdate otStatusUpdate) {
        return this.otRegistrationService.updateOtStatus(id, otStatusUpdate);
    }

    @PatchMapping("/{id}")
    public OtRegistrationResponse updateOtRegistration(@PathVariable Long id, @RequestBody OtRegistrationRequest otRegistrationRequest) {
        return this.otRegistrationService.updateOtRegistration(id, otRegistrationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteOtRegistration(@PathVariable Long id) {
        this.otRegistrationService.deleteOtRegistration(id);
    }
}
