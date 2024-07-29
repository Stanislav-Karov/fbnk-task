package org.example.cashdesk.service;

import org.example.cashdesk.exception.InsufficientFundsException;
import org.example.cashdesk.exception.InvalidOperationException;
import org.example.cashdesk.model.CashBalance;
import org.example.cashdesk.model.CashOperation;
import org.example.cashdesk.model.Denomination;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.example.cashdesk.enums.Currency.BGN;
import static org.example.cashdesk.enums.Currency.EUR;
import static org.example.cashdesk.enums.OperationType.DEPOSIT;
import static org.example.cashdesk.enums.OperationType.WITHDRAWAL;

@Service
public class CashOperationService {

    private CashBalance balanceBGN;
    private CashBalance balanceEUR;
    private static final String TRANSACTION_HISTORY_FILE = "src/main/resources/transaction_history.txt";
    private static final String CASH_BALANCE_FILE = "src/main/resources/cash_balance.txt";

    public CashOperationService() {
        List<Denomination> denominationsBGN = new ArrayList<>();
        denominationsBGN.add(new Denomination(50, 10));
        denominationsBGN.add(new Denomination(10, 50));
        balanceBGN = new CashBalance(new BigDecimal("1000"), BGN, denominationsBGN);

        List<Denomination> denominationsEUR = new ArrayList<>();
        denominationsEUR.add(new Denomination(100, 10));
        denominationsEUR.add(new Denomination(20, 50));
        balanceEUR = new CashBalance(new BigDecimal("2000"), EUR, denominationsEUR);

        updateBalanceFile();
    }

    public String processOperation(CashOperation operation) {
        validateCashOperation(operation);
        CashBalance targetBalance = operation.getCurrency().equals(BGN) ? balanceBGN : balanceEUR;

        if (operation.getType().equals(DEPOSIT)) {
            targetBalance.setTotalAmount(targetBalance.getTotalAmount().add(operation.getAmount()));
            updateDenominations(targetBalance.getDenominations(), operation.getDenominations(), true);
        } else if (operation.getType().equals(WITHDRAWAL)) {
            targetBalance.setTotalAmount(targetBalance.getTotalAmount().subtract(operation.getAmount()));
            updateDenominations(targetBalance.getDenominations(), operation.getDenominations(), false);
        }

        logTransaction(operation);
        updateBalanceFile();
        return "Operation processed successfully";
    }

    public List<CashBalance> getBalance() {
        List<CashBalance> balances = new ArrayList<>();
        balances.add(balanceBGN);
        balances.add(balanceEUR);
        return balances;
    }

    private void validateCashOperation(CashOperation operation) {
        if (operation == null) {
            throw new InvalidOperationException("Cash operation cannot be null.");
        }
        if (operation.getCurrency() == null ||
                !operation.getCurrency().equals(BGN) &&
                        !operation.getCurrency().equals(EUR)) {
            throw new InvalidOperationException("Currency must be either BGN or EUR.");
        }
        if (operation.getType() == null ||
                !operation.getType().equals(DEPOSIT) &&
                        !operation.getType().equals(WITHDRAWAL)) {
            throw new InvalidOperationException("Operation type must be either DEPOSIT or WITHDRAWAL.");
        }
        if (operation.getAmount() == null || operation.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Operation amount must be positive.");
        }
        if (operation.getDenominations() == null || operation.getDenominations().isEmpty()) {
            throw new InvalidOperationException("Denominations cannot be null or empty.");
        }
        if (operation.getAmount().compareTo(calculateTotalAmount(operation.getDenominations())) != 0) {
            throw new InvalidOperationException("Total amount of denominations does not match operation amount.");
        }
    }

    private BigDecimal calculateTotalAmount(List<Denomination> denominations) {
        return denominations.stream()
                .map(d -> BigDecimal.valueOf(d.getValue() * d.getCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateDenominations(List<Denomination> target, List<Denomination> source, boolean isDeposit) throws InsufficientFundsException {
        for (Denomination srcDenom : source) {
            if (srcDenom.getValue() <= 0 || srcDenom.getCount() <= 0) {
                throw new InvalidOperationException("Invalid denomination: value and count must be positive.");
            }
            boolean foundMatch = false;
            for (Denomination tgtDenom : target) {
                if (tgtDenom.getValue() == srcDenom.getValue()) {
                    foundMatch = true;
                    if (isDeposit) {
                        tgtDenom.setCount(tgtDenom.getCount() + srcDenom.getCount());
                    } else {
                        if (tgtDenom.getCount() < srcDenom.getCount()) {
                            throw new InsufficientFundsException("Insufficient denominations for withdrawal.");
                        }
                        tgtDenom.setCount(tgtDenom.getCount() - srcDenom.getCount());
                    }
                    break;
                }
            }
            if (!foundMatch && !isDeposit) {
                throw new InsufficientFundsException("Insufficient denominations for withdrawal.");
            }
        }
    }

    private void logTransaction(CashOperation operation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_HISTORY_FILE, true))) {
            writer.write(operation.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new InvalidOperationException("An error occurred while processing transaction.");
        }
    }

    private void updateBalanceFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CASH_BALANCE_FILE))) {
            writer.write("BGN Balance: " + balanceBGN.getTotalAmount() + "\n");
            for (Denomination denom : balanceBGN.getDenominations()) {
                writer.write(denom.getValue() + " BGN: " + denom.getCount() + "\n");
            }
            writer.write("EUR Balance: " + balanceEUR.getTotalAmount() + "\n");
            for (Denomination denom : balanceEUR.getDenominations()) {
                writer.write(denom.getValue() + " EUR: " + denom.getCount() + "\n");
            }
        } catch (IOException e) {
            throw new InvalidOperationException("An error occurred while processing the operation.");
        }
    }
}

