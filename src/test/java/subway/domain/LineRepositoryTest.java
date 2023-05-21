package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.dto.SectionDto;
import subway.dao.entity.LineEntity;
import subway.domain.repository.LineRepository;
import subway.exception.LineNotFoundException;

@ExtendWith(MockitoExtension.class)
class LineRepositoryTest {

    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @InjectMocks
    private LineRepository lineRepository;

    @Test
    @DisplayName("완성된 Line 도메인을 조회한다.")
    void findById_success() {
        // given
        given(lineDao.findById(anyLong())).willReturn(Optional.of(new LineEntity(2L, "3호선", "orange")));
        given(sectionDao.findAllSectionsWithStationNameByLineId(anyLong())).willReturn(List.of(
                new SectionDto(1L, 1L, 2L, "양재역", "남부터미널역", 10),
                new SectionDto(2L, 2L, 3L, "남부터미널역", "교대역", 10)));
        // when
        Line line = lineRepository.findById(2L);

        // then
        assertThat(line).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Line(2L, "3호선", "orange", new Sections(List.of(
                        new Section(1L, new Station(1L, "양재역"), new Station(2L, "남부터미널역"), 10),
                        new Section(2L, new Station(2L, "남부터미널역"), new Station(3L, "교대역"), 10)
                ))));
    }

    @Test
    @DisplayName("존재하지 않는 노선을 조회하면 예외가 발생한다.")
    void findById_fail() {
        // given
        given(lineDao.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> lineRepository.findById(2L))
                .isInstanceOf(LineNotFoundException.class);
    }

    @Test
    @DisplayName("구간 정보가 없는 노선을 조회할 수 있다.")
    void findByIdWithNoSections_success() {
        // given
        given(lineDao.findById(anyLong())).willReturn(Optional.of(new LineEntity(2L, "3호선", "orange")));

        // when
        Line line = lineRepository.findByIdWithNoSections(2L);

        // then
        assertThat(line).usingRecursiveComparison()
                .isEqualTo(new Line(2L, "3호선", "orange"));
    }
}