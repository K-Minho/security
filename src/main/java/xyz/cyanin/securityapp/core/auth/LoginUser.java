package xyz.cyanin.securityapp.core.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginUser {
    Long id;
    String role;
}
