package xyz.cyanin.securityapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import xyz.cyanin.securityapp.dto.UserRequest;
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
    public User 회원가입(UserRequest.JoinDTO joinDTO){ // 내부적인 프로세스 통신이면 한글로 써도 문제 없음
        String rawPassword = joinDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword); // password는 60바이트 length 60!
        joinDTO.setPassword(encPassword);
        return userRepository.save(joinDTO.toEntity()); // 
    }
}
