package com.example.tickets.controller;

import com.example.tickets.model.Ticket;
import com.example.tickets.model.TicketArray;
import com.example.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @SneakyThrows
    @GetMapping("/all")
    public ResponseEntity<List<TicketArray>> getAllTicket() {
        var result = ticketService.getAllTicket();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/average-time")
    public ResponseEntity<BigDecimal> averageTime() {
        var result = ticketService.averageTime();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
