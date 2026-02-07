package org.example.mapper;

import org.example.dto.SchedulePoint;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transaction toEntity(TransactionRequest request);

    @Mapping(source = "budget.account", target = "account")
    TransactionResponse toResponse(Transaction transaction);


}
