package com.major.user_service.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @Min(18)
    private Integer age;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String password;
}
