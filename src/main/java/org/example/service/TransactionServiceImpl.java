package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.entity.Transaction;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.TransactionMapper;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository repository;
    private final TransactionMapper mapper;


    @Transactional
    @Override
    public void addTransaction(TransactionRequest request) {
        Transaction transaction = mapper.toEntity(request);

        repository.save(transaction);
    }

    @Transactional
    @Override
    public void deleteTransaction(Long id) {

        if(repository.existsById(id)){
            repository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Transaction not found. No transaction with this ID.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = repository.findAll();

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("No transactions in database.");
        }
        else {
            return transactions.stream().map(mapper::toResponse).toList();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCategory(String category) {
        List<Transaction> transactions = repository.findAllByCategory(category);

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions not found. No transactions with this category.");
        }
        else {
            return transactions.stream().map(mapper::toResponse).toList();
        }

    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found. No transaction with this ID."));

        return mapper.toResponse(transaction);
    }

    @Transactional
    @Override
    public List<TransactionResponse> getAllByAmount(Long amount) {

        List<Transaction> transactions = repository.findAllByAmount(amount);

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions not found. No transactions with this amount.");
        }
        else {
            return transactions.stream().map(mapper::toResponse).toList();
        }

    }

    @Transactional
    @Override
    public List<TransactionResponse> getAllByCreatedAt(LocalDate createdAt) {

        List<Transaction> transactions = repository.findAllByCreatedAt(createdAt);

        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("Transactions not found. No transactions with this date.");
        }
        else {
            return transactions.stream().map(mapper::toResponse).toList();
        }
    }


}
