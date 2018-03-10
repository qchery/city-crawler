package com.qchery.city.crawler;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * 记录城市数据
 */
public class AreaDaoPipeline implements Pipeline {

    private SqlSessionFactory sqlSessionFactory;

    public AreaDaoPipeline(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> itemsAll = resultItems.getAll();
            AreaMapper mapper = session.getMapper(AreaMapper.class);
            for (Map.Entry<String, Object> entry : itemsAll.entrySet()) {
                mapper.insert((Area) entry.getValue());
            }
        }
    }
}
