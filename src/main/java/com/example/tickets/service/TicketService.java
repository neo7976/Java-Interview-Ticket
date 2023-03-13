package com.example.tickets.service;

import com.example.tickets.model.TicketArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
            log.info("Найден файл для чтения: " + FILE_NAME);
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
        var list = addListTime(ChronoUnit.MINUTES);
        log.info("Получили список времени полетов. Вычисляем среднее время полета");
        double result = 0;
        for (Long aLong : list) {
            result += aLong;
        }

        BigDecimal resultDecimal = new BigDecimal(result / (list.size() * 60));
        return resultDecimal.setScale(2, RoundingMode.UP);
    }

    @SneakyThrows
    public List<Long> addListTime(ChronoUnit chronoUnit) {
        log.info("Вычисляем время полетов в ед.: " + chronoUnit);
        var list = getAllTicket();
        var arrival = list.stream().map(x -> x.getTickets()
                        .stream().map(y -> LocalDateTime.parse(
                                y.getArrivalDate() + " " + y.getArrivalTime(), dateTimeFormatter))
                        .collect(Collectors.toList()))
                .findFirst()
                .get();

        //отправление
        var departure = list.stream().map(x -> x.getTickets()
                        .stream().map(y -> LocalDateTime.parse(
                                y.getDepartureDate() + " " + y.getDepartureTime(), dateTimeFormatter))
                        .collect(Collectors.toList()))
                .findFirst()
                .get();


        List<Long> ldt = new ArrayList<>();
        for (int i = 0; i < departure.size(); i++) {
            var start = departure.get(i);
            var end = arrival.get(i);
            ldt.add(Math.abs(chronoUnit.between(end, start)));
        }
        return ldt;
    }

    public Long percentile(double percentile) {
        log.info("Вычисляем процентиль равный " + percentile);
        var list = addListTime(ChronoUnit.MINUTES);
        Collections.sort(list);
        int index = (int) Math.ceil(percentile / 100.0 * list.size());
        return list.get(index - 1);
    }
}
