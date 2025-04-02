package com.librarymanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.TransactionDTO;
import com.librarymanagement.entity.Transaction;
import com.librarymanagement.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction(
                transactionDTO.getUserId(), 
                transactionDTO.getBookId(), 
                transactionDTO.getStatus()
        );
        transaction = transactionRepository.save(transaction);
        transactionDTO.setId(transaction.getId());
        return transactionDTO;
    }

    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    TransactionDTO dto = new TransactionDTO();
                    dto.setId(transaction.getId());
                    dto.setUserId(transaction.getUserId());
                    dto.setBookId(transaction.getBookId());
                    dto.setStatus(transaction.getStatus());
                    return dto;
                })
                .orElse(null);
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream().map(transaction -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(transaction.getId());
            dto.setUserId(transaction.getUserId());
            dto.setBookId(transaction.getBookId());
            dto.setStatus(transaction.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    public String updateTransactionStatus(Long id, String status) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setStatus(status);
            transactionRepository.save(transaction);
            return "Transaction updated successfully.";
        }).orElse("Transaction not found.");
    }
}
