package subway.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.common.RepositoryTest;
import subway.infrastructure.persistence.entity.LineEntity;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineDao 은(는)")
@RepositoryTest
class LineDaoTest {

    @Autowired
    private LineDao lineDao;

    @Test
    void 노선을_저장한다() {
        // given
        final LineEntity lineEntity = new LineEntity("1호선");

        // when
        lineDao.save(lineEntity);

        // then
        assertThat(lineDao.findAll()).hasSize(1);
    }

    @Test
    void 이름으로_노선을_조회한다() {
        // given
        final LineEntity lineEntity = new LineEntity("1호선");
        lineDao.save(lineEntity);

        // when & then
        assertThat(lineDao.findByName("1호선")).isPresent();
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        lineDao.save(new LineEntity("1호선"));
        lineDao.save(new LineEntity("2호선"));
        lineDao.save(new LineEntity("3호선"));

        // when & then
        assertThat(lineDao.findAll()).hasSize(3);
    }
}
