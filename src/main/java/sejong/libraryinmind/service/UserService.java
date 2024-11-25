package sejong.libraryinmind.service;

import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.UserEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.libraryinmind.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession session;

    public List<UserEntity> getList(){
        return this.userRepository.findAll();
    }

    public void create(String name, String username, String password){
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        this.userRepository.save(userEntity);
    }

    public Optional<UserEntity> validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
        // 사용자 검증 후, 사용자 객체 반환
    }
    // 사용자 이름으로 사용자 찾기
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void updateUser(Long userId, String name, String username, String password) {
        UserEntity user = getUserById(userId); // User 조회

        // 빌더 패턴을 사용하여 객체 수정
        UserEntity updatedUser = UserEntity.builder()
                .id(user.getId())
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(password)) // 암호화된 비밀번호
                .build();

        userRepository.save(updatedUser); // 수정된 사용자 정보 저장
    }

    public UserEntity getLoggedInUser() {
        return (UserEntity) session.getAttribute("user");
    }

    public String getLoggedInUsername() {
        UserEntity user = getLoggedInUser();
        return (user != null) ? user.getUsername() : null;
    }

    public String getLoggedInName() {
        UserEntity user = getLoggedInUser();
        return (user != null) ? user.getName() : null;
    }
    public Long getLoggedInId() {
        UserEntity user = getLoggedInUser();
        return (user != null) ? user.getId() : null;
    }
}