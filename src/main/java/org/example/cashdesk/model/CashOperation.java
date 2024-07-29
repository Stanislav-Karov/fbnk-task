package org.example.cashdesk.model;

import lombok.Getter;
import lombok.Setter;
import org.example.cashdesk.enums.Currency;
import org.example.cashdesk.enums.OperationType;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@Setter
public class CashOperation {

    private UUID transactionId;
    private OperationType type;
    private Currency currency;
    private BigDecimal amount;
    private List<Denomination> denominations;

    public CashOperation(OperationType type, Currency currency, BigDecimal amount, List<Denomination> denominations) {
        this.transactionId = UUID.randomUUID();
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.denominations = denominations;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        joiner.add("transactionId=" + transactionId)
                .add("type=" + type)
                .add("currency=" + currency)
                .add("amount=" + amount);

        if (denominations != null && !denominations.isEmpty()) {
            joiner.add("denominations=[");
            for (Denomination denomination : denominations) {
                joiner.add(denomination.toString());
            }
            joiner.add("]");
        }
        return joiner.toString();
    }
}
