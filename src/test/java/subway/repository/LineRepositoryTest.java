package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private SectionDao sectionDao;

    @Test
    void 노선을_저장한다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));

        // when
        lineRepository.save(line);

        // then
        assertThat(sectionDao.findAll()).hasSize(4);
    }
}
