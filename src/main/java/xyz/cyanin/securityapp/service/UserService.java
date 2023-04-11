package xyz.cyanin.securityapp.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import xyz.cyanin.securityapp.core.advice.MyExceptionAdvice;
import xyz.cyanin.securityapp.core.jwt.MyJwtProvider;
import xyz.cyanin.securityapp.dto.UserRequest;
import xyz.cyanin.securityapp.dto.UserResponse;
import xyz.cyanin.securityapp.dto.UserRequest.LoginDTO;
import xyz.cyanin.securityapp.dto.UserResponse.JoinDTO;
import xyz.cyanin.securityapp.model.User;
import xyz.cyanin.securityapp.model.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 1. 트랜젝션 관리
    // 2. 영속성 객체 변경감지
    // 3. RequestDTO 요청받기
    // 4. 비즈니스 로직 처리
    // 5. ResponseDTO 응답하기

    @Transactional
    public JoinDTO 회원가입(UserRequest.JoinDTO joinDTO){ // 내부적인 프로세스 통신이면 한글로 써도 문제 없음
        String rawPassword = joinDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword); // password는 60바이트 length 60!
        joinDTO.setPassword(encPassword);
        User userPS = userRepository.save(joinDTO.toEntity());
        return new UserResponse.JoinDTO(userPS); // 
    }


    public String 로그인(UserRequest.LoginDTO loginDTO) {
        Optional<User> userOP = userRepository.findByUsername(loginDTO.getUsername());
        User userPS = userOP.get();
        if (passwordEncoder.matches(loginDTO.getPassword(), userPS.getPassword())) {
            String jwt = MyJwtProvider.create(userPS);
            return MyJwtProvider.TOKEN_PREFIX + jwt;
        } else {
            throw new RuntimeException("fail to create token");
        }
    }
}
