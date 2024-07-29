package org.example.cashdesk.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Denomination {

    @NotNull
    @Positive
    private int value;
    @NotNull
    @Positive
    private int count;

    @Override
    public String toString() {
        return "value=" + value +
                ", count=" + count;
    }
}
