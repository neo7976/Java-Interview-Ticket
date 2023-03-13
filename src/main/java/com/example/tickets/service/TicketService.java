package com.example.tickets.service;

import com.example.tickets.model.Ticket;
import com.example.tickets.model.TicketArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");
    private final String DIRECTORY_PATH = "src/main/resources/templates";
    private final String FILE_NAME = "tickets.json";

    ObjectMapper objectMapper;

    public List<TicketArray> getAllTicket() throws IOException {
        Path path = Paths.get(DIRECTORY_PATH, FILE_NAME);
        File file = new File(path.toUri());
        if (file.exists()) {
            System.out.println("Файл найден");
        }
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        var ticket = objectMapper.readValue(file, new TypeReference<List<TicketArray>>() {
        });
        if (ticket.isEmpty()) {
            return Collections.emptyList();
        }
        return ticket;
    }

    @SneakyThrows
    public BigDecimal averageTime() {
        var list = getAllTicket();

        //прибытие
        var arrival = list.stream().map(x -> x.getTickets()
                        .stream().map(y -> LocalDateTime.parse(y.getArrivalDate() + " " + y.getArrivalTime(), dateTimeFormatter))
                        .collect(Collectors.toList()))
                .findFirst()
                .get();

        //отправление
        var departure = list.stream().map(x -> x.getTickets()
                        .stream().map(y -> LocalDateTime.parse(y.getDepartureDate() + " " + y.getDepartureTime(), dateTimeFormatter))
                        .collect(Collectors.toList()))
                .findFirst()
                .get();

        double result = 0;
        for (int i = 0; i < departure.size(); i++) {
            var start = departure.get(i);
            var end = arrival.get(i);
            result += Math.abs(ChronoUnit.MINUTES.between(end, start));
        }
        System.out.println(result);
        String.format("%.2f", result);

        BigDecimal resultDecimal = new BigDecimal(result / (departure.size() * 60));
        return resultDecimal = resultDecimal.setScale(2, RoundingMode.UP);

    }
}
