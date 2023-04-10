package xyz.cyanin.securityapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import xyz.cyanin.securityapp.dto.ResponseDTO;
import xyz.cyanin.securityapp.dto.UserRequest;
import xyz.cyanin.securityapp.dto.UserResponse;
import xyz.cyanin.securityapp.service.UserService;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final UserService userService;
    
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
