package subway.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static subway.fixture.LineFixture.line2WithOneSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.domain.Line;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@ExtendWith(MockitoExtension.class)
class DbLineRepositoryTest {

    @InjectMocks
    private DbLineRepository lineRepository;
    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;

    @DisplayName("새로운 Line 정보를 DB에 저장한다.")
    @Test
    void shouldSaveLineWhenRequest() {
        LineEntity lineEntity = new LineEntity(1L, "2호선");
        given(lineDao.insert(any())).willReturn(lineEntity);
        given(lineDao.findById(any())).willReturn(lineEntity);
        given(sectionDao.findAllByLineId(any())).willReturn(List.of(
                new SectionEntity(1L, 1L, 1L, 2L, 5)
        ));
        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, 1L, "잠실역")));
        given(stationDao.findById(2L)).willReturn(Optional.of(new StationEntity(2L, 1L, "몽촌토성역")));

        Line expectedLine = lineRepository.create(line2WithOneSection());

        assertThat(expectedLine.getId()).isEqualTo(1L);
        assertThat(expectedLine.getName()).isEqualTo("2호선");
        assertThat(expectedLine.getSections().get(0).getUpwardStation().getName()).isEqualTo("잠실역");
    }

    @DisplayName("ID로 Line을 조회한다.")
    @Test
    void shouldFindLineWhenInputId() {
        LineEntity lineEntity = new LineEntity(1L, "2호선");
        given(lineDao.findById(any())).willReturn(lineEntity);
        given(sectionDao.findAllByLineId(any())).willReturn(List.of(
                new SectionEntity(1L, 1L, 1L, 2L, 5)
        ));
        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, 1L, "잠실역")));
        given(stationDao.findById(2L)).willReturn(Optional.of(new StationEntity(2L, 1L, "몽촌토성역")));

        Line expectedLine = lineRepository.findById(1L);

        assertThat(expectedLine.getId()).isEqualTo(1L);
        assertThat(expectedLine.getName()).isEqualTo("2호선");
        assertThat(expectedLine.getSections().get(0).getUpwardStation().getName()).isEqualTo("잠실역");
    }

    @DisplayName("모든 Line을 조회한다.")
    @Test
    void shouldFindAllLinesWhenRequest() {
        LineEntity lineEntity = new LineEntity(1L, "2호선");
        given(lineDao.findAll()).willReturn(new ArrayList<>(List.of(lineEntity)));
        given(lineDao.findById(any())).willReturn(lineEntity);
        given(sectionDao.findAllByLineId(any())).willReturn(List.of(
                new SectionEntity(1L, 1L, 1L, 2L, 5)
        ));
        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, 1L, "잠실역")));
        given(stationDao.findById(2L)).willReturn(Optional.of(new StationEntity(2L, 1L, "몽촌토성역")));

        List<Line> expectedLines = lineRepository.findAll();

        assertThat(expectedLines).hasSize(1);
        assertThat(expectedLines.get(0).getId()).isEqualTo(1L);
    }

    @DisplayName("Line 정보를 업데이트한다.")
    @Test
    void shouldUpdateLineWhenRequest() {
        LineEntity lineEntity = new LineEntity(1L, "2호선");
        given(lineDao.findById(any())).willReturn(lineEntity);
        given(lineDao.update(any())).willReturn(lineEntity);
        given(sectionDao.findAllByLineId(any())).willReturn(List.of(
                new SectionEntity(1L, 1L, 1L, 2L, 5)
        ));
        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, 1L, "잠실역")));
        given(stationDao.findById(2L)).willReturn(Optional.of(new StationEntity(2L, 1L, "몽촌토성역")));

        Line expectedLine = lineRepository.update(line2WithOneSection());

        assertThat(expectedLine.getId()).isEqualTo(1L);
        assertThat(expectedLine.getName()).isEqualTo("2호선");
        assertThat(expectedLine.getSections().get(0).getUpwardStation().getName()).isEqualTo("잠실역");
    }
}
