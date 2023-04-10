package xyz.cyanin.securityapp.dto;



import lombok.Getter;
import lombok.Setter;
import xyz.cyanin.securityapp.model.User;

public class UserRequest {
    
    @Getter
    @Setter
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
        private String role;

        // jpa hibernate 는 insert 해야 하는 것들은 entity 가 필요함
        public User toEntity() {
            return User.builder().username(username).password(password).email(email).role(role).status(true).build();
        }
    }
}
