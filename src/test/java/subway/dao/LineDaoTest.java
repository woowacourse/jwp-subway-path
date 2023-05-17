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
            //given
            final LineEntity lineEntity = lineDao.save(new LineEntity("2호선", "초록색", 300));

            //when
            final Optional<LineEntity> line = lineDao.findById(lineEntity.getId());

            //then
            assertAll(
                    () -> assertThat(line.get().getId()).isEqualTo(lineEntity.getId()),
                    () -> assertThat(line.get().getName()).isEqualTo(lineEntity.getName()),
                    () -> assertThat(line.get().getColor()).isEqualTo(lineEntity.getColor()),
                    () -> assertThat(line.get().getExtraFare()).isEqualTo(lineEntity.getExtraFare())
            );
        }

        @Test
        @DisplayName("존재하지 않는 ID라면 빈 값을 반환한다.")
        void findByWithInvalidId() {
            //given
            //when
            final Optional<LineEntity> line = lineDao.findById(-3L);

            //then
            assertThat(line).isEmpty();
        }
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void findAll() {
        //given
        final LineEntity lineTwo = lineDao.save(new LineEntity("2호선", "초록색", 300));
        final LineEntity lineFour = lineDao.save(new LineEntity("4호선", "하늘색", 400));

        //when
        final List<LineEntity> lines = lineDao.findAll();

        //then
        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.get(0).getId()).isEqualTo(lineTwo.getId()),
                () -> assertThat(lines.get(0).getName()).isEqualTo(lineTwo.getName()),
                () -> assertThat(lines.get(0).getColor()).isEqualTo(lineTwo.getColor()),
                () -> assertThat(lines.get(0).getExtraFare()).isEqualTo(lineTwo.getExtraFare()),
                () -> assertThat(lines.get(1).getId()).isEqualTo(lineFour.getId()),
                () -> assertThat(lines.get(1).getName()).isEqualTo(lineFour.getName()),
                () -> assertThat(lines.get(1).getColor()).isEqualTo(lineFour.getColor()),
                () -> assertThat(lines.get(1).getExtraFare()).isEqualTo(lineFour.getExtraFare())
        );
    }

    @Nested
    @DisplayName("노선 정보 업데이트시 ")
    class Update {

        @Test
        @DisplayName("존재하는 노선이라면 정보를 업데이트한다.")
        void update() {
            //given
            final LineEntity line = lineDao.save(new LineEntity("2호선", "초록색", 300));

            //when
            final int numberOfUpdatedRow = lineDao.update(new LineEntity(line.getId(), "4호선", "하늘색", 300));

            //then
            final LineEntity updatedLine = lineDao.findById(line.getId()).get();
            assertAll(
                    () -> assertThat(numberOfUpdatedRow).isEqualTo(1),
                    () -> assertThat(updatedLine.getName()).isEqualTo("4호선"),
                    () -> assertThat(updatedLine.getColor()).isEqualTo("하늘색"),
                    () -> assertThat(updatedLine.getExtraFare()).isEqualTo(300)
            );
        }

        @Test
        @DisplayName("존재하지 않는 노선이라면 0을 반환한다.")
        void updateWithNotExistLine() {
            //given
            //when
            final int numberOfUpdatedRow = lineDao.update(new LineEntity(1L, "4호선", "하늘색", 300));

            //then
            assertThat(numberOfUpdatedRow).isEqualTo(0);
        }
    }
}
