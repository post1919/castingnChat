package castingn.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @Class Name : ViewConfog.java
 * @Description : ViewResolver 등록
 * @Modification Information
 * @
 * @  수정일         수정자                   수정내용
 * @ -------    --------    ---------------------------
 * @ 2021.03.02  차성순         최초 생성
 *
 *  @author 개발팀 차성순
 *  @since 2021.03.02
 *  @version 1.0
 *  @see 
 *  
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"castingn.com.chat"})
@EnableTransactionManagement
public class ViewConfig  extends WebMvcConfigurerAdapter {

	@Bean
	public ViewResolver customViewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setViewClass(JstlView.class);
		internalResourceViewResolver.setPrefix("/WEB-INF/views/");
		internalResourceViewResolver.setSuffix(".jsp");

		return internalResourceViewResolver;
    }
	
	@Bean
    MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
         registry.addResourceHandler("/**")
            .addResourceLocations("/")
            .setCachePeriod(0);
    }
}
