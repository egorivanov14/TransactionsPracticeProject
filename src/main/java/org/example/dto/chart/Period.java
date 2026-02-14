package org.example.dto.chart;

import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum Period {
    THREE_DAYS(3L, ChronoUnit.DAYS),
    WEEK(7L, ChronoUnit.DAYS),
    MONTH(30L, ChronoUnit.DAYS),
    QUARTER(90L, ChronoUnit.DAYS),
    HALF_YEAR(182L, ChronoUnit.DAYS),
    YEAR(365L, ChronoUnit.DAYS);

    @Getter
    private final Long days;

    private final ChronoUnit unit;

    private Period(Long days, ChronoUnit unit){
        this.days = days;
        this.unit = unit;
    }

    public LocalDate getStartDate(){
        return LocalDate.now().minus(days, unit);
    }

}
