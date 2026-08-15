package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Repository.CreditCardRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditCardGeneralService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardGetService.class);
    private final CreditCardRepository creditCardRepository;


}
