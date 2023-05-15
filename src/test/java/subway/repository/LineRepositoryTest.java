package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class LineRepositoryTest {

    @InjectMocks
    private LineRepository lineRepository;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @Test
    @DisplayName("새롭게 등록된 노선(구간이 하나도 없음)을 이름으로 불러온다.")
    void load_init_line_by_Name() {
        // given
        LineEntity lineEntity = new LineEntity(1L, "2호선", "#123456");
        doReturn(Optional.of(lineEntity)).when(lineDao)
                .findByName(any(String.class));
        doReturn(List.of()).when(sectionDao)
                .findByLineId(any(Long.class));

        // when
        Line result = lineRepository.findByName(lineEntity.getName());

        // then
        assertThat(result.getId()).isEqualTo(lineEntity.getId());
        assertThat(result.getName()).isEqualTo(lineEntity.getName());
        assertThat(result.getColor()).isEqualTo(lineEntity.getColor());
        assertThat(result.getSections().getSections().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("노선(구간이 존재함)을 이름으로 불러온다.")
    void load_line_by_Name() {
        // given
        LineEntity lineEntity = new LineEntity(1L, "2호선", "#123456");
        List<SectionStationEntity> sectionEntities = List.of(new SectionStationEntity(1L, 1L, "잠실", 2L, "선릉", 10));
        doReturn(Optional.of(lineEntity)).when(lineDao)
                .findByName(any(String.class));
        doReturn(sectionEntities).when(sectionDao)
                .findByLineId(any(Long.class));

        // when
        Line result = lineRepository.findByName(lineEntity.getName());

        // then
        assertThat(result.getId()).isEqualTo(lineEntity.getId());
        assertThat(result.getName()).isEqualTo(lineEntity.getName());
        assertThat(result.getColor()).isEqualTo(lineEntity.getColor());
        assertThat(result.getSections().getSections().size()).isEqualTo(1);
    }

}