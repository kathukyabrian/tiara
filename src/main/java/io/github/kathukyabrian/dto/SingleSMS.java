package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SingleSMS {
    private String to;
    private String message;
}
