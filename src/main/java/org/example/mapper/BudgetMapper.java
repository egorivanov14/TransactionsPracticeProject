package org.example.mapper;

import org.example.dto.budget.BudgetRequest;
import org.example.dto.budget.BudgetResponse;
import org.example.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Budget toEntity(BudgetRequest request);

    BudgetResponse toResponse(Budget budget);

}
