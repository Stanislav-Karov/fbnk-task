package org.example.cashdesk.controller;

import jakarta.validation.Valid;
import org.example.cashdesk.model.CashBalance;
import org.example.cashdesk.model.CashOperation;
import org.example.cashdesk.service.CashOperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CashOperationsController {

    private final CashOperationService cashOperationService;

    public CashOperationsController(CashOperationService cashOperationService) {
        this.cashOperationService = cashOperationService;
    }

    @PostMapping("/cash-operation")
    public ResponseEntity<String> processCashOperation(@Valid @RequestBody CashOperation cashOperation) {
        return ResponseEntity.ok(cashOperationService.processOperation(cashOperation));
    }

    @GetMapping("/cash-balance")
    public ResponseEntity<List<CashBalance>> getBalance() {
        return new ResponseEntity<>(cashOperationService.getBalance(), HttpStatus.OK);
    }
}
