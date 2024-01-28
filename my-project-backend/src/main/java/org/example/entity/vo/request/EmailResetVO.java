package org.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailResetVO {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
    @Length(max = 20, min = 6)
    String password;
}
