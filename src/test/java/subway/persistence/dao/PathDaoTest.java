package subway.persistence.dao;

import org.junit.jupiter.api.Test;
import subway.persistence.dao.entity.PathEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathDaoTest extends DaoTest {

    @Test
    void 모든_경로를_조회한다() {
        final List<PathEntity> paths = pathDao.findAll();

        assertThat(paths).hasSize(3);
    }

    @Test
    void 노선의_모든_경로를_삭제한다() {
        pathDao.deleteByLineId(2L);

        assertThat(pathDao.findAll()).hasSize(1);
    }

}
