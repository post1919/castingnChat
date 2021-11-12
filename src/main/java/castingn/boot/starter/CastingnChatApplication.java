package castingn.boot.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@EnableRedisHttpSession
//@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"castingn.boot.config", "castingn.boot.listner"})
public class CastingnChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(CastingnChatApplication.class, args);
	}

}
