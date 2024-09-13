package sejong.libraryinmind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.libraryinmind.entity.CustomerEntity;
import sejong.libraryinmind.repository.CustomerRepository;

@SpringBootTest
class BookApplicationTests {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void testJpa(){
		CustomerEntity customerEntity = CustomerEntity.builder()
				.customer_username("name")
				.customer_password("password")
				.build();

		this.customerRepository.save(customerEntity);
	}

}
