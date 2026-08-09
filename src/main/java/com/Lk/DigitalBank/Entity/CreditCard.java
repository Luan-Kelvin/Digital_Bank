package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Exception.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "creditCards", schema = "entitys")
@Getter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @ToString.Include
    private String cardNumber;

    @Column(nullable = false, updatable = false)
    @ToString.Include
    private String cvv;

    @Column(nullable = false)
    @ToString.Include
    private LocalDate expirationDate = LocalDate.now().plusYears(5);

    @Column(nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal avalialableLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal usedLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private CardStatus cardStatus = CardStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate dueDay = LocalDate.now().plusDays(20);;

    @Column(nullable = false, length = 4)
    private String password;

    @OneToOne(mappedBy = "creditCard")
    private Account account;

    public CreditCard(String password, String cardNumber, Account account) {
        verifyPassword(password);
        this.password = password;
        this.cardNumber = cardNumber;
        this.cvv = cardNumber.split(" ")[3];
        this.account = account;
        this.account.addCreditCard(this);
    }

    // VERIFICAR SENHA
    private  void verifyPassword(String password){
        if (password.length() != 4){
            throw new InvalidCreditCradPINException("ERRO! Senha deve conter exatamente 4 digitos");
        }

        if (!password.matches("^[0-9]{4}$")){
            throw new InvalidCreditCradPINException("ERRO! senha deve conter apenas números.");
        }

        if (password.equalsIgnoreCase("1234")){
            throw new InvalidCreditCradPINException("ERRO! Senha fraca, não pode ser "+password);
        }
    }

    //METODO PRIVADO PARA VERIFICAR SE SENHA É COMPATÍVEL
    private  void checkCompatibility(String password){
        if (!this.password.equals(password)){
            throw new InvalidPasswordException("ERRO! Senha inválida");
        }
    }

    //BLOQUEAR CARTÃO
    public void blockCard(String password){
       checkCompatibility(password);
        cardStatus = CardStatus.BLOCKED;
    }

    //DESBLOQUEAR CARTÃO
    public void unlockCard(String password){
        checkCompatibility(password);
        this.cardStatus = CardStatus.ACTIVE;
    }

    // CANCELAR CARTÃO
    public void cancelCard(String password){
        checkCompatibility(password);

        cardStatus = CardStatus.CANCELED;
    }

    //ALTERAR SENHA
    public void changePassword(String cpfCustomer, String oldPassword, String newPassword){
        String cpf = account.getCustomer().getCpf();

        if (!cpfCustomer.equals(cpf)){
            throw new InvalidCPFException("ERRO! CPF diferente do titular da conta.");
        }

        checkCompatibility(oldPassword);
        verifyPassword(newPassword);

        this.password = newPassword;
    }

    //AUMENTAR LIMITE
    public void increaseLimit(BigDecimal value){
        if (value.compareTo(BigDecimal.valueOf(100)) <= 0){
            throw new InvalidLimitValueException("ERRO! valor mínimo para aumento de limite é de R$100,00");
        }

        this.creditLimit = creditLimit.add(value);
        this.avalialableLimit = avalialableLimit.add(value);
    }

    // DIMINUIR LIMITE
    public void lowerLimit(BigDecimal value){
        if (value.compareTo(BigDecimal.valueOf(100)) <= 0){
            throw new InvalidLimitValueException("ERRO! valor mínimo para redução de limite é de R$100,00");
        }

        if (value.compareTo(this.creditLimit) == 1){
            throw new InvalidLimitValueException("ERRO! Valor para redução de limite é menor que o valor atual do limite.");
        }

        this.creditLimit = creditLimit.subtract(value);
        this.avalialableLimit = avalialableLimit.subtract(value);
    }

    //VERIFICAR SE TEM LIMITE DISPONÍVEL PARA  COMPRA
    public boolean hasLimit(BigDecimal purchaseValue){
        if (purchaseValue.compareTo(avalialableLimit) == 1){
            return false;
        }else {
            return true;
        }
    }

    //REGISTRAR COMPRA PARA DIMINUIR LIMITE DISPONÍVEL
    public void purchase(BigDecimal value){
        avalialableLimit = avalialableLimit.subtract(value);
        usedLimit = usedLimit.add(value);
    }

    // PAGAR FATURA
    public void payTheBill(){
        usedLimit = BigDecimal.ZERO;
        avalialableLimit = creditLimit;
    }
}
