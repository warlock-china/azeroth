package cn.com.warlock.common2.excel;

import java.util.List;

public interface ResultProcessor<T> {

    int batchSize();

    void process(List<T> datas);

}
