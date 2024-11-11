package sejong.libraryinmind.service;

import org.springframework.beans.factory.annotation.Autowired;
import sejong.libraryinmind.entity.CustomerEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.libraryinmind.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<CustomerEntity> getList(){
        return this.customerRepository.findAll();
    }

    public void create(String username, String password){
        CustomerEntity customerEntity = CustomerEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        this.customerRepository.save(customerEntity);
    }

    public boolean validateUser(String username, String password) {
        return customerRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword())) // 암호화된 비밀번호 검증
                .orElse(false);
    }

    @Transactional
    public void delete(Long id){
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객은 존재하지 않습니다. id = "+ id));

        this.customerRepository.delete(customerEntity);
    }

}