package subway.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.LineEntity;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineRepositoryTest {

    @Mock
    LineDao lineDao;

    @Mock
    SectionDao sectionDao;

    LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new LineRepository(lineDao, sectionDao);
    }

    @Test
    void insert_메소드는_line을_저장하고_저장한_데이터를_반환한다() {
        final Line line = Line.of("12호선", "bg-red-500");
        given(lineDao.insert(any(LineEntity.class))).willReturn(LineEntity.of(1L, line.getName(), line.getColor()));


        final Line actual = lineRepository.insert(line);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo(line.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(line.getColor())
        );
    }

    @Test
    void insert_메소드는_지정한_노선_이름이_이미_존재하는_경우_예외가_발생한다() {
        final Line line = Line.of("12호선", "bg-red-500");
        given(lineDao.existsByName(anyString())).willReturn(true);

        assertThatThrownBy(() -> lineRepository.insert(line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지정한 노선의 이름은 이미 존재하는 이름입니다.");
    }

    @Test
    void findById_메소드는_저장되어_있는_id를_전달하면_해당_line을_반환한다() {
        final LineEntity lineEntity = LineEntity.of(1L, "12호선", "bg-red-500");
        given(lineDao.findById(anyLong())).willReturn(Optional.of(lineEntity));

        final Optional<Line> actual = lineRepository.findById(1L);

        assertThat(actual).isPresent();
    }

    @Test
    void findById_메소드는_없는_id를_전달하면_빈_Optional을_반환한다() {
        given(lineDao.findById(anyLong())).willReturn(Optional.empty());

        final Optional<Line> actual = lineRepository.findById(-999L);

        assertThat(actual).isEmpty();
    }

    @Test
    void findAll_메소드는_호출하면_모든_line을_반환한다() {
        final LineEntity lineEntity = LineEntity.of(1L, "12호선", "bg-red-500");
        given(lineDao.findAll()).willReturn(List.of(lineEntity));

        final List<Line> actual = lineRepository.findAll();

        assertThat(actual).hasSize(1);
    }

    @Test
    void deleteById_메소드는_id를_전달하면_해당_id를_가진_line을_삭제한다() {
        assertDoesNotThrow(() -> lineRepository.deleteById(1L));
    }
}
