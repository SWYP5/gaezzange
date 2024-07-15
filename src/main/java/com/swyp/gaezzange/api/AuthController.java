package com.swyp.gaezzange.api;

import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/token")
    public String token(String email) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return user.getToken();
    }
}
