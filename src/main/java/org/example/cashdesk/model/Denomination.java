package org.example.cashdesk.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
