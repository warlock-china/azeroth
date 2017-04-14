package cn.com.warlock.mybatis.page;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class PageTemplate {

    public static <T> PageInfo<T> execute(int pageNo, int pageSize, PageDataLoader<T> dataLoader) {
        PageHelper.startPage(pageNo, pageSize);
        List<T> list = dataLoader.load();
        return new PageInfo<T>(list);
    }
}
