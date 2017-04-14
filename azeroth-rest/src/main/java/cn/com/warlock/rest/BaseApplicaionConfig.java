package cn.com.warlock.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.web.filter.RequestContextFilter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import cn.com.warlock.rest.excetion.BaseExceptionMapper;
import cn.com.warlock.rest.filter.DefaultWebFilter;
import cn.com.warlock.rest.filter.FilterConfig;
import cn.com.warlock.rest.resolver.ObjectMapperResolver;
import cn.com.warlock.rest.resolver.ValidationContextResolver;
import io.swagger.jaxrs.listing.SwaggerSerializers;

public abstract class BaseApplicaionConfig extends ResourceConfig implements CustomConfig {

    public BaseApplicaionConfig() {
        //设置默认时区
        System.setProperty("user.timezone", "Asia/Shanghai");

        register(ValidationContextResolver.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);

        this.packages(packages());
        register(ObjectMapperResolver.class);
        register(JacksonFeature.class);
        register(JacksonJsonProvider.class);
        register(new BaseExceptionMapper(createExcetionWrapper()));
        register(RequestContextFilter.class);

        register(DefaultWebFilter.class);

        if (FilterConfig.apiDocEnabled()) {
            register(SwaggerSerializers.class);
        }
    }

}
