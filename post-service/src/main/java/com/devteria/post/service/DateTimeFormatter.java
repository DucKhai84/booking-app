package com.devteria.post.service;


import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {

    Map<Long, Function<Instant, String>> stategyMap = new LinkedHashMap<>(
            60
    );

    public DateTimeFormatter(){
        stategyMap.put(60L, this::formatSeconds);
        stategyMap.put(3600L, this::formatMinutes);
        stategyMap.put(86400L, this::formatHours);
        stategyMap.put(Long.MIN_VALUE, this::formatInDate);

    }

    public String format(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        var strategy = stategyMap.entrySet()
                .stream()
                .filter(longFunctionEntry -> elapseSeconds < longFunctionEntry.getKey())
                .findFirst().get();

        return strategy.getValue().apply(instant);
    }

    private String formatSeconds(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return elapseSeconds + " seconds";
    }

    private String formatMinutes(Instant instant){
        long elapMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return elapMinutes + " minutes";
    }

    private String formatHours(Instant instant){
        long elapHours = ChronoUnit.HOURS.between(instant, Instant.now());
        return elapHours + " hours";
    }

    private String formatInDate(Instant instant){
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }
}
