package org.example.cashdesk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.cashdesk.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CashBalance {

    private BigDecimal totalAmount;
    private Currency currency;
    private List<Denomination> denominations;

    @Override
    public String toString() {
        return "CashBalance{" +
                "totalAmount=" + totalAmount +
                ", currency=" + currency +
                ", denominations=" + denominations +
                '}';
    }
}
