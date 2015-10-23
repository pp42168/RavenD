package @orgPath@.@prj@.common.util;

import org.springframework.beans.BeanUtils;

public class BeanAndDtoTransfer {
	/**
	 * 将dto转换为Bean
	 * @author lonaking
	 * @param dto
	 * @param cla
	 * @return
	 */
	public static <D, B> B PutDtoIntoBean(D dto , Class<B> cla){
			B bean;
			try {
				bean = cla.newInstance();
				BeanUtils.copyProperties(dto, bean);
				return bean;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	/**
	 * 将Bean转换为Dto
	 * @author lonaking
	 * @param bean
	 * @param cla
	 * @return
	 */
	public static <B, D> D putBeanIntoDto(B bean, Class<D> cla){
		try {
			D dto = cla.newInstance();
			BeanUtils.copyProperties(bean, dto);
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
