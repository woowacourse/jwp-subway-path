package subway.line.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.domain.Line;
import subway.section.persistence.SectionDao;

@ExtendWith(MockitoExtension.class)
class H2LineRepositoryTest {

  @Mock
  private LineDao lineDao;

  @Mock
  private SectionDao sectionDao;

  @InjectMocks
  private H2LineRepository h2LineRepository;

  @Test
  void createLine() {
    given(lineDao.findByName(any())).willReturn(Optional.empty());

    h2LineRepository.createLine("2호선");

    assertDoesNotThrow(() -> h2LineRepository.createLine("2호선"));
  }

  @Test
  void createExistedLine() {
    given(lineDao.findByName(any())).willReturn(Optional.of(new LineEntity(1L, "2호선")));

    assertThatThrownBy(() -> h2LineRepository.createLine("2호선"));
  }

  @Test
  void findById() {
    final Long lineId = 1L;
    final LineEntity lineEntity = new LineEntity(lineId, "2호선");
    given(lineDao.findById(lineEntity.getId())).willReturn(Optional.of(lineEntity));

    final Line line = h2LineRepository.findById(lineId);
    assertThat(line.getId()).isEqualTo(lineEntity.getId());
    assertThat(line.getLineName()).isEqualTo(lineEntity.getLineName());
  }

  @Test
  void findByExistedId() {
    given(lineDao.findById(any())).willReturn(Optional.empty());

    assertThatThrownBy(() -> h2LineRepository.findById(1L))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void updateLine() {
    h2LineRepository.updateLine(new Line(1L, "2호선"));

    verify(sectionDao, times(1)).deleteAllByLineId(any());
    verify(sectionDao, times(1)).insertAll(any());
  }

  @Test
  void findAll() {
    given(lineDao.findAll()).willReturn(List.of(
        new LineEntity(1L, "2호선"),
        new LineEntity(2L, "3호선")
    ));

    final List<Line> lines = h2LineRepository.findAll();

    assertThat(lines.size()).isEqualTo(2);
  }
}
