package castingn.com.chat.mapper.mall;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MallMapper {


	/**
	 * 사용자 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectMallMemberInfo(Map<String, Object> param) throws Exception;

	/**
	 * 하위 사용자 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectMallSubMemberInfo(Map<String, Object> param) throws Exception;

	/**
	 * 파트너사 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectMallCompanyInfo(Map<String, Object> param) throws Exception;

	/**
	 * 관리자 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectMallAdminInfo(Map<String, Object> param) throws Exception;

	/**
	 * 서비스 상품 정보 조회
	 * @param Map<String, Object>
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	Map<String, Object> selectMallProductInfo(Map<String, Object> param) throws Exception;

	Map<String, Object> selectMallCustomerInfo(Map<String, Object> param);

}
