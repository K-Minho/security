package xyz.cyanin.securityapp.core.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import xyz.cyanin.securityapp.model.User;

@Getter
public class MyUserDetails implements UserDetails {

    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 인가가 필요할때의 권한(역할) 체크
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(()-> "ROLE_" + user.getRole());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { // n년이상 지난 휴면 계정등과 같은 활성화 여부
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() { // 비밀번호 오류 n회시 계정 잠금
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 자격 증명 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() { // 탈퇴 여부
        return user.getStatus();
    }
}
