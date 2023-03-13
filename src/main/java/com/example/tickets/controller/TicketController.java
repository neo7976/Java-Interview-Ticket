package com.example.tickets.controller;

import com.example.tickets.model.Ticket;
import com.example.tickets.model.TicketArray;
import com.example.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String averageTime() {
        var result = ticketService.averageTime();
        return String.valueOf(result);
    }

    @SneakyThrows
    @GetMapping("/percentile")
    public String percentile(@RequestParam("value") double value) {
        var result = ticketService.percentile(value);
        return String.valueOf(result);
    }
}
