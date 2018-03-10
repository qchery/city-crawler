package com.qchery.city.crawler.pipeline;


import com.qchery.city.crawler.entity.Area;
import com.qchery.city.crawler.mapper.AreaMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * 记录城市数据
 */
public class AreaMapperPipeline implements Pipeline {

    private SqlSessionFactory sqlSessionFactory;

    public AreaMapperPipeline(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> itemsAll = resultItems.getAll();
            AreaMapper mapper = session.getMapper(AreaMapper.class);
            for (Map.Entry<String, Object> entry : itemsAll.entrySet()) {
                Area area = (Area) entry.getValue();
                if (area.getCode() != null && area.getCode().length() > 6) {
                    area.setCode(area.getCode().substring(0, 6));
                }
                mapper.insert(area);
            }
        }
    }
}
