package com.Lk.DigitalBank.HandlerExceptions;

import com.Lk.DigitalBank.DTOs.DTOException.ErrorResponse;
import com.Lk.DigitalBank.Exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestAdvince {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> accountExists(AccountAlreadyExistsException e, HttpServletRequest request){

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conta existente",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(AccountAlreadyHasCreditCardException.class)
    public ResponseEntity<ErrorResponse> accountAlreadyHasCard(
            AccountAlreadyHasCreditCardException e,
            HttpServletRequest request)
    {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conta já possui cartão de crédito.",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccountDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> accounNotExists(AccountDoesNotExistException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Conta não existe",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<ErrorResponse> accountInative(
            AccountInactiveException e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conta não está ativa.",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(CardIsNotActiveForPurchasseException.class)
    public ResponseEntity<ErrorResponse> cardNotActive(
            CardIsNotActiveForPurchasseException e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Cartão está inativo",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(CreditCardsNotExistException.class)
    public ResponseEntity<ErrorResponse> creditCardNotExists(
            CreditCardsNotExistException e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Cartão de crédito não existe",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> customerAlreadyExists(
            CustomerAlreadyExistsException e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Cliente ja existe no banco",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(CustomerDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> customerNotExists(
            CustomerDoesNotExistException e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Cliente não existe",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(InsufficientBalanceWithdraw.class)
    public ResponseEntity<ErrorResponse> insufficientBalanceWithdraw(
            InsufficientBalanceWithdraw e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Saldo Insuficiente para saque",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(InsufficientLimitException.class)
    public ResponseEntity<ErrorResponse> insufficientLimit(InsufficientLimitException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Limite insuficiente",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(InvalidCPFException.class)
    public ResponseEntity<ErrorResponse> invalidCPF(InvalidCPFException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "CPF Inválido",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(InvalidCreditCreditPinException.class)
    public ResponseEntity<ErrorResponse> invalidCreditCardPin(
            InvalidCreditCreditPinException e, HttpServletRequest request
    ){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Senha do cartão de crédito inválida",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(InvalidDepositAmountException.class)
    public ResponseEntity<ErrorResponse> invalidDepositAmount(InvalidDepositAmountException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Valor de depósito inválido",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(InvalidLimitValueException.class)
    public ResponseEntity<ErrorResponse> invalidLimitValue(InvalidLimitValueException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Valor de limite inválido",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> invalidPassword(InvalidPasswordException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Senha inválida",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MinorClientException.class)
    public ResponseEntity<ErrorResponse> minorClient(MinorClientException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Cliente não pode ter menos que 18 anos",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<ErrorResponse> invalidAccountStatus(InvalidAccountStatusException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Status de conta inexistente",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<ErrorResponse> invalidAccountType(InvalidAccountTypeException e, HttpServletRequest request){
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Tipo de conta inexistente",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> vlidationError(MethodArgumentNotValidException e, HttpServletRequest request){

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dado inválido",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
