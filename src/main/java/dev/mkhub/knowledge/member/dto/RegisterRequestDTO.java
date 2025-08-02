package dev.mkhub.knowledge.member.dto;

import dev.mkhub.knowledge.common.validation.PasswordMatch;
import jakarta.validation.constraints.AssertTrue;
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
@PasswordMatch
public class RegisterRequestDTO {

    private String username;        // 이메일 값이 들어감.

    @NotBlank(message = "{NotBlank.registerRequestDTO.nickname}")
    @Size(min = 2, max = 20, message = "{Size.registerRequestDTO.nickname}")
    private String nickname;

    @NotBlank(message = "{NotBlank.registerRequestDTO.email}")
    @Email(message = "{Email.registerRequestDTO.email}")
    private String email;           // 물론 별도의 이메일 필드가 존재하므로 필요함.

    @NotBlank(message = "{NotBlank.registerRequestDTO.password}")
    @Size(min = 8, message = "{sizePassword}")
    private String password;

    @NotBlank(message = "{NotBlank.registerRequestDTO.confirmPassword}")
    private String confirmPassword;

    @AssertTrue(message = "{email.checked}")
    private boolean emailChecked;       // email 중복사용 체크 여부

    @AssertTrue@AssertTrue(message = "{nickname.checked}")
    private boolean nicknameChecked;  // nickname 중복사용 체크 여부

    //username에 email이 들어가도록 셋업
    public void setUsername(String email) {
        this.username = email;
    }
}
