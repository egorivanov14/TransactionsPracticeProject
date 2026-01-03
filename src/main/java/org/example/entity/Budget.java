package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

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

//    @Column(nullable = false)
//    private LocalDate startDate;
//
//    @Column(nullable = false)
//    private LocalDate endDate;
//
//    @Column(nullable = false)
//    private String periodType;

    @PrePersist
    protected void onCreate(){
        spend = 0L;
    }

}

