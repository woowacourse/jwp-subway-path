package subway.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.anySet;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.SectionEntity.Builder;
import subway.persistence.entity.StationEntity;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class SectionRepositoryTest {

    @Mock
    SectionDao sectionDao;

    @Mock
    StationDao stationDao;

    Line line;
    SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        line = Line.of(1L, "12호선", "bg-red-500");
        final Station upStation = Station.of(1L, "12역");
        final Station downStation = Station.of(2L, "23역");
        line.createSection(upStation, downStation, Distance.from(5), Direction.DOWN);
    }

    @Test
    void insert_메소드는_line에_저장된_section을_저장한다() {
        assertDoesNotThrow(() -> sectionRepository.insert(line));
    }

    @Test
    void findAllByLine_메소드는_지정한_line과_일치하는_모든_section을_조회해_line에_저장하고__line을_반환한다() {
        final SectionEntity sectionEntity = Builder.builder()
                .id(1L)
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        final StationEntity upStationEntity = StationEntity.of(1L, "12역");
        final StationEntity downStationEntity = StationEntity.of(2L, "23역");
        given(sectionDao.findAllByLineId(1L)).willReturn(List.of(sectionEntity));
        given(stationDao.findAllByIds(anySet())).willReturn(List.of(upStationEntity, downStationEntity));

        final Line persistLine = sectionRepository.findAllByLine(line);
        final Map<Station, Section> actual = persistLine.getSections().sections();

        final Station upStation = upStationEntity.to();
        final Station downStation = downStationEntity.to();
        assertAll(
                () -> assertThat(actual.get(upStation).findDirectionByStation(downStation)).isEqualTo(Direction.DOWN),
                () -> assertThat(actual.get(upStation).findDistanceByStation(downStation).distance()).isEqualTo(5),
                () -> assertThat(actual.get(downStation).findDirectionByStation(upStation)).isEqualTo(Direction.UP),
                () -> assertThat(actual.get(downStation).findDistanceByStation(upStation).distance()).isEqualTo(5)
        );
    }
}
