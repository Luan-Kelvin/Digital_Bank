package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.DTOs.Account.TransferPixDTO;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionGetDTO;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionPixDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Entity.Transaction;
import com.Lk.DigitalBank.Exception.*;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountPostService {
    private final Logger logger = LoggerFactory.getLogger(AccountGetService.class);
    private final Conversor conversor;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final NumberGenerator numberGenerator;

    // CRIAR CONTA
    public AccountGetDTO createAccount(AccountPostDTO accountPostDTO){
        Customer customer = customerRepository.findByCpf(accountPostDTO.cpfCustomer())
                .orElseThrow(() -> new CustomerDoesNotExistException("ERRO! Cliente não existe no banco."));

        if (accountRepository.existsByCustomerAndAccountType(customer, accountPostDTO.accountType())){
            throw new AccountAlreadyExistsException("ERRO! Conta ja existente.");
        }

        Account account = new Account(customer, accountPostDTO.accountType());
        account.addNumberAccount(numberGenerator.generateNumberAccount());

        customer.addAccount(account);

        accountRepository.save(account);
        logger.info("Conta "+account.getAccountNumber()+" criada com sucesso!");

        return conversor.converterAccount(account);
    }

    // FAZER PIX
    @Transactional
    public TransactionPixDTO transferViaPix(TransferPixDTO dto) {

        Account recipient = accountRepository.findByAccountNumber(dto.recipientAccount())
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com Nº %s não existe.", dto.recipientAccount())));

        Account sender = accountRepository.findByAccountNumber(dto.senderAccount())
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com Nº %s não existe", dto.senderAccount())));

        if (!recipient.isActive()){
            throw new AccountInactiveException(String.format("ERRO! Conta Nº %s  esta INATIVA.", dto.recipientAccount()));
        }

        if (!sender.isActive()){
            throw new AccountInactiveException(String.format("ERRO! Conta Nº %s  esta INATIVA.", dto.senderAccount()));
        }

        if (dto.value().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("ERRO! Valor de transferência deve ser maior que 0.");
        }

        if (sender.getAccountNumber().equals(recipient.getAccountNumber())){
            throw new PixTransferFailedException("ERRO! Contas precisam ser diferentes para realizar PIX.");
        }

        Transaction transactionSender = sender.makeAPixTransfer(recipient, dto.value());
        Transaction transactionRecipient = recipient.receivePixTransfer(sender, dto.value());
        accountRepository.save(sender);
        accountRepository.save(recipient);
        transactionRepository.save(transactionSender);
        transactionRepository.save(transactionRecipient);

        logger.info(String.format("%s fez um pix no valor de R$%s para %s.", sender.getCustomer().getName(), dto.value(), recipient.getCustomer().getName()));

        return new TransactionPixDTO(
                LocalDateTime.now(),
                dto.value(),
                String.format
                        ("Pix enviado por %s para %s no valor de R$%s",
                                sender.getCustomer().getName(),
                                recipient.getCustomer().getName(),
                                dto.value()),
                sender.getAccountNumber(),
                recipient.getAccountNumber()
                );
    }
}
