package castingn.boot.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Class Name : DataSourceConfig.java
 * @Description : 데이터베이스 정보 객체
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
@ConfigurationProperties("castingn.datasource.chat")
@MapperScan(basePackages = {"castingn.com.chat.mapper.chat", "castingn.com.chat.mapper.mall"}, sqlSessionFactoryRef = "sqlSessionFactory", sqlSessionTemplateRef = "sqlSession")
public class DataSourceConfig {
	
	private String driverClassName;
	private String jdbcUrl;
	private String username;
	private String password;
	private String mybatisConfigPath;
	private String mybatisMapperLocations;
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMybatisConfigPath() {
		return mybatisConfigPath;
	}

	public void setMybatisConfigPath(String mybatisConfigPath) {
		this.mybatisConfigPath = mybatisConfigPath;
	}

	public String getMybatisMapperLocations() {
		return mybatisMapperLocations;
	}

	public void setMybatisMapperLocations(String mybatisMapperLocations) {
		this.mybatisMapperLocations = mybatisMapperLocations;
	}
	
	@Primary
	@Bean(name="dataSource")
	public DataSource baseDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}

	@Primary
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();

		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setConfigLocation(applicationContext.getResource(mybatisConfigPath));
		sessionFactoryBean.setMapperLocations(resolver.getResources(mybatisMapperLocations));

		return sessionFactoryBean.getObject();
	}

	@Primary
	@Bean("sqlSession")
	public SqlSessionTemplate sqlSessionTemplate(@Autowired @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory ) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Primary
	@Bean("transactionManager")
	public PlatformTransactionManager transactionManager(@Autowired @Qualifier("dataSource") DataSource dataSource){ 
		return new DataSourceTransactionManager(dataSource); 
	}
}
