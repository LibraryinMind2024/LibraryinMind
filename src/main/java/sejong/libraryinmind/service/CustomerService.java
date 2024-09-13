package sejong.libraryinmind.service;

import sejong.libraryinmind.entity.CustomerEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.libraryinmind.repository.CustomerRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerEntity> getList(){
        return this.customerRepository.findAll();
    }

    public void create(String username, String password){
        CustomerEntity customerEntity = CustomerEntity.builder()
                .customer_username(username)
                .customer_password(password)
                .build();
        this.customerRepository.save(customerEntity);
    }

    @Transactional
    public void delete(Long id){
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객은 존재하지 않습니다. id = "+ id));

        this.customerRepository.delete(customerEntity);
    }

}