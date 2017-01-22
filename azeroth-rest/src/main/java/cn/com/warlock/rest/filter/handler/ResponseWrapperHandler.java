package cn.com.warlock.rest.filter.handler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MediaType;

import cn.com.warlock.rest.filter.FilterHandler;
import cn.com.warlock.rest.response.ResponseCode;
import cn.com.warlock.rest.response.WrapperResponseEntity;

public class ResponseWrapperHandler implements FilterHandler {


	@Override
	public void processRequest(ContainerRequestContext requestContext, HttpServletRequest request,
			ResourceInfo resourceInfo) {}

	@Override
	public void processResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext,
			ResourceInfo resourceInfo) {
		MediaType mediaType = responseContext.getMediaType();
		if (mediaType != null && MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
			Object responseData = responseContext.getEntity();
			WrapperResponseEntity jsonResponse;

			if (responseData instanceof WrapperResponseEntity) {
				jsonResponse = (WrapperResponseEntity) responseData;
			} else {
				jsonResponse = new WrapperResponseEntity(ResponseCode.OK);
				jsonResponse.setData(responseData);
			}
			responseContext.setStatus(ResponseCode.OK.getCode());

			responseContext.setEntity(jsonResponse);

		}
	}

	@Override
	public int getPriority() {
		return 9;
	}

}
