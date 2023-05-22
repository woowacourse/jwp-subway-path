package subway.infrastructure.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.core.domain.Line;
import subway.application.core.domain.LineProperty;
import subway.infrastructure.dao.LineDao;
import subway.infrastructure.dao.SectionDao;
import subway.infrastructure.dao.StationDao;
import subway.infrastructure.entity.LineRow;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class H2LineRepositoryTest {

    @InjectMocks
    private H2LineRepository lineRepository;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private LineDao lineDao;

    @Test
    @DisplayName("ID를 통해 Line을 찾을 수 있다")
    void findById() {
        // given
        when(lineDao.findById(any())).thenReturn(new LineRow(1L, "1호선", "파랑"));

        // when
        Line found = lineRepository.findById(1L);

        // then
        assertThat(found).isNotNull();
    }

    @Test
    @DisplayName("모든 Line을 찾을 수 있다")
    void findAll() {
        // given
        when(lineDao.selectAll()).thenReturn(List.of(
                new LineRow(1L, "1호선", "파랑"),
                new LineRow(2L, "2호선", "초록")
        ));
        when(sectionDao.selectAll()).thenReturn(Collections.emptyList());

        // when
        List<Line> found = lineRepository.findAll();

        // then
        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("Line을 삽입할 수 있다")
    void insert() {
        // given
        LineRow lineRow = new LineRow(1L, "1호선", "파랑");
        when(lineDao.findById(any())).thenReturn(lineRow);

        // when
        Line inserted = lineRepository.insert(new Line(
                new LineProperty(1L, "1호선", "파랑"),
                Collections.emptyList()
        ));

        // then
        assertThat(inserted.getId()).isEqualTo(lineRow.getId());
    }
}
