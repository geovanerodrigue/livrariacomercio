package livrariacomercio.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "livrariacomercio.web.model")
@ComponentScan(basePackages = {"livrariacomercio.*"})
@EnableJpaRepositories(basePackages = {"livrariacomercio.web.repository"})
@EnableTransactionManagement
@EnableWebMvc
public class LivrariacomercioApplication implements WebMvcConfigurer  {

	public static void main(String[] args) {
		SpringApplication.run(LivrariacomercioApplication.class, args);
	}

	//edita a tela de login para a personalizada
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
           registry.addViewController("/login").setViewName("/login");
           registry.setOrder(Ordered.LOWEST_PRECEDENCE);
	}

}
