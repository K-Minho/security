package xyz.cyanin.securityapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import xyz.cyanin.securityapp.core.auth.MyUserDetails;
import xyz.cyanin.securityapp.dto.ResponseDTO;
import xyz.cyanin.securityapp.dto.UserRequest;
import xyz.cyanin.securityapp.dto.UserResponse;
import xyz.cyanin.securityapp.service.UserService;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<?> user(@PathVariable Long id,
    @AuthenticationPrincipal MyUserDetails myUserDetails) { // principal 내부의 모든 것들을 꺼내서 사용
        String username = myUserDetails.getUser().getUsername();
        String role = myUserDetails.getUser().getRole();
        return ResponseEntity.ok().body("Hello! User " + username + "! You are " + role);
    }
    
    @GetMapping("/")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok().body("hello");
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(UserRequest.JoinDTO joinDTO) {
        UserResponse.JoinDTO data = userService.회원가입(joinDTO);
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(data);
        return ResponseEntity.ok().body(responseDTO);
    }

}
