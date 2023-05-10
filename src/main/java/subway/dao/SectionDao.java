package subway.dao;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        return Collections.emptyList();
    }
}
