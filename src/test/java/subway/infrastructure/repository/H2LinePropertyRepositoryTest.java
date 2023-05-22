package subway.infrastructure.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.core.domain.LineProperty;
import subway.infrastructure.dao.LineDao;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class H2LinePropertyRepositoryTest {

    private H2LinePropertyRepository linePropertyRepository;

    @Autowired
    public H2LinePropertyRepositoryTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        LineDao lineDao = new LineDao(jdbcTemplate, dataSource);
        this.linePropertyRepository = new H2LinePropertyRepository(lineDao);
    }

    @Test
    @DisplayName("LineProperty를 삽입할 수 있다")
    void insert() {
        // given
        LineProperty lineProperty = new LineProperty(null, "1호선", "파랑");

        // when
        LineProperty inserted = linePropertyRepository.insert(lineProperty);

        // then
        assertThat(inserted).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(lineProperty);
    }

    @Test
    @DisplayName("LineProperty를 모두 찾을 수 있다")
    void findAll() {
        // given
        linePropertyRepository.insert(new LineProperty(null, "1호선", "파랑"));
        linePropertyRepository.insert(new LineProperty(null, "2호선", "초록"));

        // when
        List<LineProperty> found = linePropertyRepository.findAll();

        // then
        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("LineProperty를 ID를 통해 찾을 수 있다")
    void findById() {
        // given
        LineProperty inserted = linePropertyRepository.insert(new LineProperty(null, "1호선", "파랑"));

        // when
        LineProperty found = linePropertyRepository.findById(inserted.getId());

        // then
        assertThat(found).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(inserted);
    }

    @Test
    @DisplayName("LineProperty를 업데이트 할 수 있다")
    void update() {
        // given
        LineProperty inserted = linePropertyRepository.insert(new LineProperty(null, "1호선", "파랑"));

        // when
        LineProperty updated = new LineProperty(inserted.getId(), "2호선", "초록");
        linePropertyRepository.update(updated);
        LineProperty found = linePropertyRepository.findById(inserted.getId());

        // then
        assertThat(found).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(updated);
    }

    @Test
    @DisplayName("LineProperty를 삭제할 수 있다")
    void deleteById() {
        // given
        LineProperty inserted = linePropertyRepository.insert(new LineProperty(null, "1호선", "파랑"));

        // when
        linePropertyRepository.deleteById(inserted.getId());
        List<LineProperty> found = linePropertyRepository.findAll();

        // then
        assertThat(found).isEmpty();
    }
}
