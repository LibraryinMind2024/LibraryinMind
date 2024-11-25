package sejong.libraryinmind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.libraryinmind.repository.UserRepository;

@SpringBootTest
class BookApplicationTests {

	@Autowired
	private UserRepository userRepository;

//	@Test
//	void testJpa(){
//		UserEntity userEntity = UserEntity.builder()
//				.customer_username("name")
//				.customer_password("password")
//				.build();
//
//		this.customerRepository.save(userEntity);
//	}

}
