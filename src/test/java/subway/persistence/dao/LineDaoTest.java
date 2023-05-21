package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.persistence.RepositoryTest;
import subway.persistence.entity.LineEntity;

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
        final LineEntity lineEntity = new LineEntity("1호선", 500);

        // when
        lineDao.save(lineEntity);

        // then
        assertThat(lineDao.findAll()).hasSize(1);
    }

    @Test
    void 이름으로_노선을_조회한다() {
        // given
        final LineEntity lineEntity = new LineEntity("1호선", 500);
        lineDao.save(lineEntity);

        // when & then
        assertThat(lineDao.findByName("1호선")).isPresent();
    }

    @Test
    void ID로_노선을_조회한다() {
        // given
        final LineEntity lineEntity = new LineEntity("1호선", 500);
        final Long id = lineDao.save(lineEntity);

        // when & then
        assertThat(lineDao.findById(id)).isPresent();
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        lineDao.save(new LineEntity("1호선", 500));
        lineDao.save(new LineEntity("2호선", 300));
        lineDao.save(new LineEntity("3호선", 200));

        // when & then
        assertThat(lineDao.findAll()).hasSize(3);
    }

    @Test
    void 노선을_제거한다() {
        // given
        final LineEntity lineEntity = new LineEntity("1호선", 400);
        final Long id = lineDao.save(lineEntity);


        // when
        lineDao.delete(new LineEntity(id, lineEntity.getName(), lineEntity.getAdditionalFee()));

        // then
        assertThat(lineDao.findByName("1호선")).isEmpty();
    }
}
