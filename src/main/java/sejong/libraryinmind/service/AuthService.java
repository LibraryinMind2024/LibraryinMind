package sejong.libraryinmind.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.util.JwtTokenProvider;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    // 요청 헤더에서 토큰을 추출하여 사용자 인증
    public UserEntity authenticateFromToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // "Bearer " 이후의 부분을 추출
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token); // 토큰에서 username 추출
                return userService.findByUsername(username).orElse(null); // 사용자 찾기
            }
        }
        return null; // 인증 실패 시 null 반환
    }
}
