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

    // 로그인한 사용자 정보 가져오기
    public UserEntity getLoggedInUser() {
        return (UserEntity) session.getAttribute("user");
    }

    // 로그인한 사용자의 이름 가져오기
    public String getLoggedInUsername() {
        UserEntity user = getLoggedInUser();
        return (user != null) ? user.getUsername() : null;
    }

    // 로그인한 사용자의 이름 가져오기
    public String getLoggedInName() {
        UserEntity user = getLoggedInUser();
        return (user != null) ? user.getName() : null;
    }
    public Long getLoggedInId() {
        UserEntity user = getLoggedInUser();
        return (user != null) ? user.getId() : null;
    }

}