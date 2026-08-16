package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Exception.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "creditCards", schema = "entitys")
@Getter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
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
    private Integer closingDayInvoice;

    @Column(nullable = false)
    private Integer dueDayInvoice;

    @Column(nullable = false, length = 4)
    private String password;

    @OneToOne(mappedBy = "creditCard")
    private Account account;

    @OneToMany(mappedBy = "creditCard")
    private List<CreditCardPurchase> creditCardPurchases = new ArrayList<>();

    @OneToMany(mappedBy = "creditCard")
    private List<Invoice> invoices = new ArrayList<>();

    public CreditCard(String password, String cardNumber, Account account, Integer closingDayInvoice) {
        verifyPassword(password);
        this.password = password;
        this.cardNumber = cardNumber;
        this.cvv = cardNumber.split(" ")[3];
        this.account = account;

        if (closingDayInvoice < 1 || closingDayInvoice > 25){
            throw new IllegalArgumentException("ERRO! Dia de fechamento da fatura deve ser no máximo ate dia 25.");
        }

        this.closingDayInvoice = closingDayInvoice;
        this.dueDayInvoice = closingDayInvoice + 5;
    }

    // VERIFICAR SENHA
    private  void verifyPassword(String password){
        if (password == null || password.length() != 4){
            throw new InvalidCreditCradPINException("ERRO! Senha deve conter exatamente 4 digitos");
        }

        if (!password.matches("^[0-9]{4}$")){
            throw new InvalidCreditCradPINException("ERRO! senha deve conter apenas números.");
        }

        if (password.equalsIgnoreCase("1234")){
            throw new InvalidCreditCradPINException("ERRO! Senha fraca, não pode ser "+password);
        }
    }

    // ADICIONAR CREDIT CARD PURCHASE
    public void addCreditCardPurchase(CreditCardPurchase purchase){
        if (purchase == null){
            throw new IllegalArgumentException("ERRO! Valor inválido");
        }

        this.creditCardPurchases.add(purchase);

        if (purchase.getCreditCard() != this){
            purchase.addCrditCard(this);
        }
    }

    //METODO PARA VERIFICAR SE SENHA É COMPATÍVEL
    public boolean checkCompatibility(String password){
        if (!this.password.equals(password)){
            return false;
        } else {
            return true;
        }
    }

    //REGISTRAR NOVA COMPRA NO CARTÃO DE CRÉDITO
    public void recordNewPurchase(CreditCardPurchase purchase){
        if (purchase == null) {
            throw new NullPointerException("ERRO! compra inválida.");
        }

        if (purchase.getCreditCard() != null && purchase.getCreditCard() != this) {
            throw new IllegalStateException(
                    "ERRO! Esta compra já pertence a outro cartão."
            );
        }

        if (!creditCardPurchases.contains(purchase)) {
            creditCardPurchases.add(purchase);
        }

        if (purchase.getCreditCard() != this) {
            purchase.addCrditCard(this);
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
    public void changePassword(String newPassword){
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

        if (value.compareTo(avalialableLimit) == 1){
            throw new InvalidLimitValueException("ERRO! Novo limite deve nao pode ser aaixo do limite utilizado.");
        }

        if (value.compareTo(this.creditLimit) == 1){
            throw new InvalidLimitValueException("ERRO! Valor para redução de limite é menor que o valor atual do limite.");
        }

        this.creditLimit = creditLimit.subtract(value);
        this.avalialableLimit = avalialableLimit.subtract(value);
    }

    //VERIFICAR SE TEM LIMITE DISPONÍVEL PARA  COMPRA
    public boolean hasLimit(BigDecimal purchaseValue){
        return purchaseValue.compareTo(avalialableLimit) <= 0;
    }

    //REGISTRAR COMPRA PARA DIMINUIR LIMITE DISPONÍVEL
    public void purchase(BigDecimal value){
        avalialableLimit = avalialableLimit.subtract(value);
        usedLimit = usedLimit.add(value);
    }


    // ALTERAR DATA DE FECHAMENTO DA FATURA;
    public void changeBillingClosingDay(Integer newDay){
        if (newDay < 1 || newDay > 25){
            throw new IllegalArgumentException("ERRO! Dia de fechamento da fatura deve ser no máximo ate dia 25.");
        }

        this.closingDayInvoice = newDay;
        this.dueDayInvoice = newDay + 5;
    }

}
