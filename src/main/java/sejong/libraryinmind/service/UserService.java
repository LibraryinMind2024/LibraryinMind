package sejong.libraryinmind.service;

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


    // 비밀번호 확인 후 이름, 아이디, 비밀번호 수정
    public UserEntity updateUserDetails(Long userId, String currentPassword, String newName, String newUsername, String newPassword) {
        // 사용자 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Builder를 사용하여 새로운 UserEntity 객체 생성
        UserEntity updatedUser = UserEntity.builder()
                .id(user.getId())  // 기존 ID 유지
                .name(newName)     // 새 이름 설정
                .username(newUsername)  // 새 아이디 설정
                .password(encodedNewPassword) // 새 비밀번호 설정
                .build();

        // 변경된 사용자 정보 저장
        return userRepository.save(updatedUser);
    }

}