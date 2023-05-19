package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Fixture;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LineRepositoryTest {

    @InjectMocks
    private LineRepository lineRepository;
    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;

    @Test
    @DisplayName("Line id를 기준으로 조회 기능")
    void findLineById() {
        // given
        final Line line = new Line(1L, "2호선", "green");
        final LineEntity lineEntity = new LineEntity(line.getId(), line.getName(), line.getColor(), Fixture.stationA.getId());
        given(lineDao.findById(line.getId())).willReturn(lineEntity);
        given(sectionDao.findByLineId(line.getId())).willReturn(List.of(Fixture.sectionAB));
        given(stationDao.findById(lineEntity.getUpEndpointId())).willReturn(Fixture.stationA);

        // when & then
        assertThat(lineRepository.findLineById(line.getId())).isEqualTo(line);
    }

    @Test
    @DisplayName("모든 Line 조회 기능")
    void findLines() {
        // given
        final Line line1 = new Line(1L, "2호선", "green");
        final LineEntity lineEntity = new LineEntity(line1.getId(), line1.getName(), line1.getColor(), Fixture.stationA.getId());
        given(lineDao.findAll()).willReturn(List.of(lineEntity));
        given(sectionDao.findByLineId(anyLong())).willReturn(List.of(Fixture.sectionAB));
        given(stationDao.findById(anyLong())).willReturn(Fixture.stationA);

        // when & then
        assertThat(lineRepository.findLines().size()).isEqualTo(1);
    }
}
