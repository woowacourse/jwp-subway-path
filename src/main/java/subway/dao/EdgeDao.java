package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.entity.EdgeEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public interface EdgeDao {
    Long save(EdgeEntity edgeEntity);

    void deleteAllEdgesOf(Long lineId);

}


