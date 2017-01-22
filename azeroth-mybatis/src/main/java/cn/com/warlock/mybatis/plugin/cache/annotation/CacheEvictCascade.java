package cn.com.warlock.mybatis.plugin.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.warlock.mybatis.core.BaseEntity;

/**
 * 缓存关联更新
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvictCascade {
	/**
	 * 级联更新其他的实体组
	 * @return
	 */
	Class<? extends BaseEntity>[] cascadeEntities() default {};
}
