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
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.entity.LineEntity;
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

    @Test
    @DisplayName("노선에 초기 두 역을 저장한다.")
    void register_() {
        // given
        List<StationEntity> stationEntities = List.of(
                new StationEntity(1L, "잠실역", 1L),
                new StationEntity(2L, "선릉역", 1L));
        doReturn(stationEntities).when(stationDao).insertInit(any(List.class));


        // when
        List<Station> result = lineRepository.saveInitStations(
                new Section(new Station("잠실역"), new Station("선릉역"), 10),
                1L);

        // then
        assertThat(result.get(0).getId()).isEqualTo(stationEntities.get(0).getId());
        assertThat(result.get(0).getName()).isEqualTo(stationEntities.get(0).getName());
        assertThat(result.get(1).getId()).isEqualTo(stationEntities.get(1).getId());
        assertThat(result.get(1).getName()).isEqualTo(stationEntities.get(1).getName());
    }

    @Test
    @DisplayName("이름과 노선 아이디를 통해 역을 찾는다.")
    void find_station_by_name_line_id() {
        // given
        StationEntity stationEntity = new StationEntity(1L,"잠실", 1L);
        doReturn(Optional.of(stationEntity)).when(stationDao).findByNameAndLineId(any(String.class), any(Long.class));

        // when
        Station result = lineRepository.findByNameAndLineId("잠실", 1L);

        // then
        assertThat(result.getId()).isEqualTo(stationEntity.getId());
        assertThat(result.getName()).isEqualTo(stationEntity.getName());
    }
}