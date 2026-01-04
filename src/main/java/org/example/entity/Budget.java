package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long limitAmount;

    @Column(nullable = false)
    private Long spend;

    @Column(nullable = false)
   private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    @PrePersist
    protected void onCreate(){
        spend = 0L;

        if(startDate == null){
            startDate = LocalDate.now();
        }
        if(endDate == null){

            if(periodType==PeriodType.MONTHLY){
                endDate = LocalDate.now().plusMonths(1);
            }
            else if(periodType==PeriodType.WEEKLY){
                endDate = LocalDate.now().plusWeeks(1);
            }
            else if(periodType==PeriodType.DAILY){
                endDate = LocalDate.now().plusDays(1);
            }
            else {
                endDate = LocalDate.now().plusMonths(1);
            }
        }
    }

}

