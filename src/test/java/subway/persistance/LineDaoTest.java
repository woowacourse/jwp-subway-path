package subway.persistance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import subway.Fixture;
import subway.domain.Line;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@ContextConfiguration(classes = LineDao.class)
@Sql("/schema.sql")
class LineDaoTest {

    @Autowired
    private LineDao lineDao;

    @Test
    @DisplayName("노선을 추가한다")
    void insert() {
        // given & when
        final Line inserted = lineDao.insert(Fixture.line1);

        // then
        assertThat(inserted.getName()).isEqualTo(Fixture.line1.getName());
        assertThat(inserted.getColor()).isEqualTo(Fixture.line1.getColor());
    }

    @Test
    @DisplayName("모든 노선을 조회한다")
    void findAll() {
        // given
        lineDao.insert(Fixture.line1);
        lineDao.insert(Fixture.line2);
        lineDao.insert(Fixture.line3);

        // when
        final List<Line> lines = lineDao.findAll();

        // then
        assertThat(lines.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("id에 해당하는 노선을 조회한다")
    void findById() {
        // given
        final Line inserted1 = lineDao.insert(Fixture.line1);
        final Line inserted2 = lineDao.insert(Fixture.line2);
        final Line inserted3 = lineDao.insert(Fixture.line3);

        // when
        final Optional<Line> found = lineDao.findById(inserted2.getId());

        // then
        assertThat(found.get().getName()).isEqualTo(Fixture.line2.getName());
        assertThat(found.get().getColor()).isEqualTo(Fixture.line2.getColor());
    }

    @Test
    @DisplayName("id에 해당하는 노선을 삭제한다")
    void deleteById() {
        // given
        final Line inserted1 = lineDao.insert(Fixture.line1);
        final Line inserted2 = lineDao.insert(Fixture.line2);
        final Line inserted3 = lineDao.insert(Fixture.line3);

        // when
        lineDao.deleteById(inserted2.getId());

        // then
        assertThat(lineDao.findAll().size()).isEqualTo(2);
    }
}
