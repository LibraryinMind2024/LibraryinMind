package sejong.libraryinmind.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
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

//    public boolean validateUser(String username, String password) {
//        return userRepository.findByUsername(username)
//                .map(user -> passwordEncoder.matches(password, user.getPassword())) // 암호화된 비밀번호 검증
//                .orElse(false);
//    }

    public Optional<UserEntity> validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
        // 비밀번호 검증 후, 사용자 객체 반환
    }


    @Transactional
    public void delete(Long id){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객은 존재하지 않습니다. id = "+ id));

        this.userRepository.delete(userEntity);
    }

}