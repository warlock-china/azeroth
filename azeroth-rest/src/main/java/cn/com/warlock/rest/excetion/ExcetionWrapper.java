package cn.com.warlock.rest.excetion;

import cn.com.warlock.rest.response.WrapperResponseEntity;

public interface ExcetionWrapper {

    WrapperResponseEntity toResponse(Exception e);
}
