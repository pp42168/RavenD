package @orgPath@.@prj@.svr.web.framework;

import com.google.gson.Gson;
import me.ele.contract.exception.ServiceException;
import me.ele.elog.Log;
import me.ele.elog.LogFactory;

import @orgPath@.@prj@.common.exception.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class WebAPISecurityFilter implements Filter {

	private final static Log LOGGER = LogFactory.getLog(WebAPISecurityFilter.class);
	
	private WebAPISecurityProtocol securityProtocol = new WebAPISecurityProtocol();
	/*
	@Value("${app.python_gettoken_url}")
	private String python_gettoken_url;*/

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		corsResponse(httpResponse);

		/*验证token scm用
		String token = httpRequest.getHeader("x-token");
		String userId = "";
		int[] supplier_ids = null;
		int supplierId = -1;
		
		if (token != null && !"".equals(token)) {
			SCMHttpClient scmHttpClient = new SCMHttpClient();
			String json = scmHttpClient.getHttpClient(python_gettoken_url + token);
			CollectUserIdDto userIdDto = new CollectUserIdDto();
			Gson gson =new Gson();
			userIdDto = gson.fromJson(json, CollectUserIdDto.class);

			if (userIdDto.getData() != null) {
				userId = userIdDto.getData().getUser_id();
				supplier_ids = userIdDto.getData().getSupplier_ids();
			}
			if(supplier_ids != null && supplier_ids.length != 0){
				supplierId = supplier_ids[0];
			}
		}

		httpRequest.setAttribute("userId", userId);
		httpRequest.setAttribute("supplierId", supplierId);
		*/

		// if (httpRequest.getRequestURI().indexOf("webapi") > 0) {
		// chain.doFilter(request, response);
		// return;
		// }

		// 登陆请求，版本更新，忘记密码不做安全验证
//		if (httpRequest.getRequestURI().endsWith(WebAPISecurityProtocol.AUTH_URI) ||
//			httpRequest.getRequestURI().endsWith(WebAPISecurityProtocol.AUTH_CHECK_VERSION)||
//			httpRequest.getRequestURI().endsWith(WebAPISecurityProtocol.AUTH_RESET_EMAIL)) {
//			chain.doFilter(request, response);
//			return;
//		}
//
//		ResponseEntity<?> error = null;
//		try {
//			error = securityCheck(httpRequest, httpResponse);
//		} catch (Exception e) {
//			logger.error("security check error", e);
//			error = new ResponseEntity<String>("SECURITY_CHECK_ERROR", "安全检查失败", "请重新登录");
//		}
//		if (error != null) {
//			httpResponse.setContentType("application/json");
//			httpResponse.setCharacterEncoding("UTF-8");
//			Gson gson = new Gson();
//			httpResponse.getWriter().write(gson.toJson(error));
//			httpResponse.getWriter().flush();
//			return;
//		}
	
		chain.doFilter(request, response);
	}

	private ResponseEntity<?> securityCheck(HttpServletRequest request, HttpServletResponse response) {
		//token存放在request的hearder里
		String access_token = request.getHeader(WebAPISecurityProtocol.HTTP_ACCESS_TOKEN);

		// if (access_token == null || access_token.trim().equals("")) {
		// 	SystemException se = new SystemException(WebAPIExceptions.ERR_WEBAPI_ACCESS_TOKEN_NOT_FOUND);
		// 	LOGGER.error("access_token is null , or access_token is empty ");
		// 	return new ResponseEntity<String>(se.getErrorCode(), se.getErrorMessage(), "");
		// }

		// 1,通过token，查询coffee-hr系统，验证token的有效性
		// 获得KarmaUser对象
		//KarmaUser karmaUser = new KarmaUser();
		// 将对象存入request中
		//request.setAttribute(WebAPISecurityProtocol.KARMA_USER,karmaUser);
//
//		Session session = null;
//		try {
//			session = sessionService.checkToken(access_token);
//		} catch (SessionServiceException e) {
//			logger.error("security Check error : " + e.getMessage());
//			return new ResponseEntity<String>(e.getErrorCode(), e.getMessage(), "");
//		}
//
//		User user = session.getUser();
//		request.setAttribute(WebAPISecurityProtocol.HTTP_PRINCIPAL, user);
//		// 组装context
//		String identification = (String) Beans.toJSONStringSafe(user);
//		Context context = new Context();
//		Principal principal = new Principal();
//		principal.setType(Principal.PRINCIPAL_TYPE_USER);
//		principal.setIdentification(identification);
//		principal.setToken(access_token);
//		context.setPrincipal(principal);
//		request.setAttribute(WebAPISecurityProtocol.HTTP_CONTEXT, context);
//
		// if (!securityProtocol.checkSign(request)) {
		// 	SystemException se = new SystemException(WebAPIExceptions.ERR_BAD_REQUEST);
		// 	return new ResponseEntity<String>(se.getErrorCode(), se.getErrorMessage(), "");
		// }

		return null;
	}

	/*
	 * Cross-origin resource sharing (CORS)
	 * @param response
	 */
	private void corsResponse(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", WebAPISecurityProtocol.CUSTOM_HEADERS);
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
	}

	@Override
	public void destroy() {
	}

}
