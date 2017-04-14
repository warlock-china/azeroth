package cn.com.warlock.mybatis.page;

import java.util.List;

public interface PageDataLoader<T> {

    List<T> load();
}
