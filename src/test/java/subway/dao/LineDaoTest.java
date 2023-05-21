package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.LineEntity;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/line_initialize.sql")
@ActiveProfiles("test")
class LineDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("새로운 노선을 저장한다.")
    void insert() {
        // given
        LineEntity lineEntity = new LineEntity("5호선", "보라색");

        // when
        long savedId = lineDao.insert(lineEntity);

        // then
        Optional<LineEntity> foundLine = lineDao.findById(savedId);
        assertThat(foundLine).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new LineEntity(savedId, "5호선", "보라색"));
    }

    @Test
    @DisplayName("저장된 모든 노선 정보를 조회한다.")
    void findAll() {
        // when
        List<LineEntity> lines = lineDao.findAll();

        // then
        assertThat(lines).hasSize(3);
    }

    @ParameterizedTest
    @CsvSource(value = {"5호선:노란색:false", "2호선:노란색:true", "1호선:green:true"}, delimiter = ':')
    @DisplayName("동일한 이름 또는 색이 있는 노선이 존재하는지 확인할 수 있다.")
    void existsByNameAndColor(String name, String color, boolean isExist) {
        // when
        boolean result = lineDao.existsByNameAndColor(color, name);

        // then
        assertThat(result).isEqualTo(isExist);
    }

    @Test
    @DisplayName("노선 정보를 변경할 수 있다.")
    void update() {
        // given
        LineEntity changeEntity = new LineEntity(1L, "9호선", "갈색");

        // when
        lineDao.update(changeEntity);

        // then
        assertThat(lineDao.findById(1L)).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(changeEntity);
    }

    @Test
    @DisplayName("노선을 삭제할 수 있다.")
    void deleteById() {
        // given
        lineDao.insert(new LineEntity("9호선", "갈색"));

        // when
        lineDao.deleteById(4L);

        // then
        assertThat(lineDao.findAll()).hasSize(3);
    }
}
