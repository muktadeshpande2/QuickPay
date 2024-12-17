package com.major.transaction_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreateRequest {

    @NotBlank
    private String receiverId;

    @Min(1)              //least denomination
    private Long amount;

    private String reason;
}
