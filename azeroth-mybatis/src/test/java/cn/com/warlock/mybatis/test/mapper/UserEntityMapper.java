package cn.com.warlock.mybatis.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.com.warlock.mybatis.core.BaseMapper;
import cn.com.warlock.mybatis.plugin.cache.annotation.Cache;
import cn.com.warlock.mybatis.test.entity.UserEntity;

public interface UserEntityMapper extends BaseMapper<UserEntity, Integer> {

    @Cache
    List<UserEntity> findByType(short type);

    @Cache
    List<UserEntity> findByStatus(short status);

    @Cache
    UserEntity findByMobile(@Param("mobile") String mobile);

    @Cache
    List<UserEntity> countByExample(UserEntity user);

    @Cache
    @Select("SELECT count(*) FROM users where type=#{type}")
    int countByType(@Param("type") int type);

    int updateType(@Param("ids") int[] ids, @Param("type") int type);

    void updateType2(UserEntity user);

}