package sejong.libraryinmind.service;

import sejong.libraryinmind.entity.CustomerEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.libraryinmind.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerEntity> getList(){
        return this.customerRepository.findAll();
    }

    public void create(String username, String password){
        CustomerEntity customerEntity = CustomerEntity.builder()
                .username(username)
                .password(password)
                .build();
        this.customerRepository.save(customerEntity);
    }

    public boolean validateUser(String username, String password) {
        Optional<CustomerEntity> user = customerRepository.findByUsername(username);

        return user.isPresent() && user.get().getPassword().equals(password);
    }

    @Transactional
    public void delete(Long id){
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객은 존재하지 않습니다. id = "+ id));

        this.customerRepository.delete(customerEntity);
    }

}