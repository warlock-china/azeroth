package cn.com.warlock.rest.filter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ResourceInfo;

public interface FilterHandler {

	void processRequest(ContainerRequestContext requestContext, HttpServletRequest request,
			ResourceInfo resourceInfo);
	
	void processResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext,
			ResourceInfo resourceInfo) ;
	
	int getPriority();
}
