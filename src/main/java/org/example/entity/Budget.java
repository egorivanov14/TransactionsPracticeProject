package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budgets")
@Getter
@Setter
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private Long limitAmount;

    @Column(nullable = false)
   private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @PrePersist
    protected void onCreate(){

        if(periodType == null){
            periodType = PeriodType.MONTHLY;
        }

        if(endDate == null){

            if(PeriodType.MONTHLY.equals(periodType)){
                endDate = LocalDate.now().plusMonths(1);
            }
            else if(PeriodType.WEEKLY.equals(periodType)){
                endDate = LocalDate.now().plusWeeks(1);
            }
            else if(PeriodType.DAILY.equals(periodType)){
                endDate = LocalDate.now().plusDays(1);
            }
        }

    }

}

