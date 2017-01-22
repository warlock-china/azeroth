package cn.com.warlock.rest.filter.handler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ResourceInfo;

import cn.com.warlock.rest.RestConst;
import cn.com.warlock.rest.filter.FilterConfig;
import cn.com.warlock.rest.filter.FilterHandler;

public class CorsHandler implements FilterHandler {


	@Override
	public void processRequest(ContainerRequestContext requestContext, HttpServletRequest request,
			ResourceInfo resourceInfo) {}

	@Override
	public void processResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext,
			ResourceInfo resourceInfo) {
		responseContext.getHeaders().add(RestConst.ACCESS_CONTROL_ALLOW_ORIGIN, FilterConfig.getCorsAllowOrgin());
		responseContext.getHeaders().add(RestConst.ACCESS_CONTROL_ALLOW_METHODS_TITLE, RestConst.ACCESS_CONTROL_ALLOW_METHODS);
		responseContext.getHeaders().add(RestConst.ACCESS_CONTROL_ALLOW_HEADERS, RestConst.ALLOW_HEADERS);
		responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");	
	}

	@Override
	public int getPriority() {
		return 10;
	}

}
