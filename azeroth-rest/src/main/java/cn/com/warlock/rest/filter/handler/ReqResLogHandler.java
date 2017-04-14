package cn.com.warlock.rest.filter.handler;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ResourceInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.com.warlock.common.json.JsonUtils;
import cn.com.warlock.rest.filter.DefaultWebFilter;
import cn.com.warlock.rest.filter.FilterHandler;
import cn.com.warlock.rest.utils.RequestUtils;

public class ReqResLogHandler implements FilterHandler {

    private static final Logger logger = LogManager.getLogger(DefaultWebFilter.class);

    public void processRequest(ContainerRequestContext requestContext, HttpServletRequest request,
                               ResourceInfo resourceInfo) {
        String requestLog = buildRequestLog(requestContext, request);
        logger.info(requestLog);
    }

    public void processResponse(ContainerRequestContext requestContext,
                                ContainerResponseContext responseContext,
                                ResourceInfo resourceInfo) {
        Object responseData = responseContext.getEntity();
        logger.info("response:\n", JsonUtils.toJson(responseData));
    }

    private static String buildRequestLog(ContainerRequestContext requestContext,
                                          HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("requestURI:").append(request.getRequestURI()).append("\n");
        try {
            Map<String, Object> map = RequestUtils.getParametersMap(requestContext, request);
            if (map != null) {
                sb.append("parameter:").append("\n");
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
                }
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
