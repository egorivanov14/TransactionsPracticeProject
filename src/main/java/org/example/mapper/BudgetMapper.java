package org.example.mapper;

import org.example.dto.BudgetRequest;
import org.example.dto.BudgetResponse;
import org.example.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "id", ignore = true)
    Budget toEntity(BudgetRequest request);

    BudgetResponse toResponse(Budget budget);

}
