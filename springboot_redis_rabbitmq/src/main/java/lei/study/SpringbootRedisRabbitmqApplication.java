package lei.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("lei.study.pojo")
public class SpringbootRedisRabbitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRedisRabbitmqApplication.class, args);
	}

}
