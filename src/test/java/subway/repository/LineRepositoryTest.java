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
import subway.domain.line.Color;
import subway.domain.line.Line;
import subway.domain.line.Name;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionStationEntity;

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
        List<SectionStationEntity> sectionEntities = List.of(new SectionStationEntity(1L, 1L, "잠실", 2L, "선릉", 1L, 10));
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
    @DisplayName("역 id를 통해 노선을 찾는다.")
    void find_line_by_station_id() {
        // given
        Station station = new Station(1L, "잠실역");
        LineEntity lineEntity = new LineEntity(1L, "2호선", "#123456");
        List<SectionStationEntity> sectionEntities = List.of(new SectionStationEntity(1L, 1L, "잠실", 2L, "선릉", 1L, 10));
        doReturn(Optional.of(lineEntity)).when(lineDao)
                .findByStationId(any(Long.class));
        doReturn(sectionEntities).when(sectionDao)
                .findByLineId(any(Long.class));

        // when
        Line result = lineRepository.findByStationId(station.getId());

        // then
        assertThat(result.getId()).isEqualTo(lineEntity.getId());
        assertThat(result.getName()).isEqualTo(lineEntity.getName());
        assertThat(result.getColor()).isEqualTo(lineEntity.getColor());
        assertThat(result.getSections().getSections().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("노선 전체를 가져온다.")
    void find_all_line() {
        // given
        List<LineEntity> lineEntities = List.of(
                new LineEntity(1L, "2호선", "#123456"),
                new LineEntity(2L, "3호선", "#abcdef")
        );
        doReturn(lineEntities).when(lineDao).findAll();

        List<SectionStationEntity> sectionStationEntities = List.of(
                new SectionStationEntity(1L, 1L, "잠실", 2L, "선릉", 1L, 10),
                new SectionStationEntity(2L, 2L, "선릉", 3L, "강남", 1L, 10),
                new SectionStationEntity(3L, 4L, "수서", 5L, "가락시장", 2L, 15)
        );
        doReturn(sectionStationEntities).when(sectionDao).findAll();

        List<Line> expect = List.of(
                new Line(
                        1L,
                        new Name("2호선"),
                        new Color("#123456"),
                        new Sections(List.of(
                                new Section(1L, new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                                new Section(2L, new Station(2L, "선릉"), new Station(3L, "강남"), 10))
                        )),
                new Line(
                        2L,
                        new Name("3호선"),
                        new Color("#abcdef"),
                        new Sections(List.of(
                                new Section(3L, new Station(4L, "수서"), new Station(5L, "가락시장"), 15)
                        ))
                )
        );

        // when
        List<Line> result = lineRepository.readAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }
}
