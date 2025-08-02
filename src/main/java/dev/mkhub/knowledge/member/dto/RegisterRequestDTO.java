package dev.mkhub.knowledge.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원가입 요청 DTO
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private String username;        // 이메일 값이 들어감.

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 사이여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;           // 물론 별도의 이메일 필드가 존재하므로 필요함.

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    //username에 email이 들어가도록 셋업
    public void setUsername(String email) {
        this.username = email;
    }
}
