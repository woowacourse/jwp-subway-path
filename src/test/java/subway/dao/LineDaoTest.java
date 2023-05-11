package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Nested
    @DisplayName("아이디로 조회시 ")
    class FindById {

        @Test
        @DisplayName("존재하는 ID라면 노선 정보를 반환한다.")
        void findById() {
            final LineEntity entity = lineDao.save(new LineEntity("2호선", "초록색"));

            final Optional<LineEntity> line = lineDao.findById(entity.getId());

            assertAll(
                    () -> assertThat(line.get().getId()).isEqualTo(entity.getId()),
                    () -> assertThat(line.get().getName()).isEqualTo(entity.getName()),
                    () -> assertThat(line.get().getColor()).isEqualTo(entity.getColor())
            );
        }

        @Test
        @DisplayName("존재하지 않는 ID라면 빈 값을 반환한다.")
        void findByWithInvalidId() {
            final Optional<LineEntity> line = lineDao.findById(-3L);

            assertThat(line).isEmpty();
        }
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void findAll() {
        final LineEntity lineTwo = lineDao.save(new LineEntity("2호선", "초록색"));
        final LineEntity lineFour = lineDao.save(new LineEntity("4호선", "하늘색"));

        final List<LineEntity> lines = lineDao.findAll();

        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.get(0).getId()).isEqualTo(lineTwo.getId()),
                () -> assertThat(lines.get(0).getName()).isEqualTo(lineTwo.getName()),
                () -> assertThat(lines.get(0).getColor()).isEqualTo(lineTwo.getColor()),
                () -> assertThat(lines.get(1).getId()).isEqualTo(lineFour.getId()),
                () -> assertThat(lines.get(1).getName()).isEqualTo(lineFour.getName()),
                () -> assertThat(lines.get(1).getColor()).isEqualTo(lineFour.getColor())
        );
    }
}
