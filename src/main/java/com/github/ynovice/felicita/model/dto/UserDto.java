package com.github.ynovice.felicita.model.dto;

import com.github.ynovice.felicita.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String role;
    private String username;

    private List<OAuth2CredentialDto> oAuth2Credentials;

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setRole(user.getRole().toString());
        dto.setUsername(user.getUsername());

        dto.setOAuth2Credentials(
                user.getOAuth2Credentials()
                        .stream()
                        .map(OAuth2CredentialDto::fromEntity)
                        .toList()
        );

        return dto;
    }
}
