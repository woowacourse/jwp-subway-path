package subway.service.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.exception.NotExistException;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.dto.SectionCreateRequest;
import subway.service.section.repository.SectionRepository;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixture.LineFixture.EIGHT_LINE_NO_ID;
import static subway.fixture.LineFixture.SECOND_LINE_NO_ID;
import static subway.fixture.StationFixture.GANGNAM_NO_ID;
import static subway.fixture.StationFixture.JAMSIL_NO_ID;
import static subway.fixture.StationFixture.SEOKCHON_NO_ID;
import static subway.fixture.StationFixture.SEONLEUNG_NO_ID;
import static subway.fixture.StationFixture.YUKSAM_NO_ID;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@Sql("/schema-test.sql")
class SectionServiceTest {

    @Autowired
    SectionService sectionService;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionCaching sectionCaching;

    Line savedLine;
    Station savedJamsil;
    Station savedGangnam;

    @BeforeEach
    void setUp() {
        sectionCaching.clearSectionsCache();
        savedLine = lineRepository.insert(EIGHT_LINE_NO_ID);
        savedJamsil = stationRepository.insert(JAMSIL_NO_ID);
        savedGangnam = stationRepository.insert(GANGNAM_NO_ID);
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());
        sectionService.insert(sectionCreateRequest);
    }

    @Test
    void 존재하지_않는_노선에_역을_추가하려고_하면_예외() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 10, 20L);
        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(NotExistException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    void 노선에_존재하는_역_2개를_추가하면_예외() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가하려는 경로의 역들은 이미 노선에 존재하는 역들입니다.");

    }

    @Test
    void 동일한_역_2개를_추가하면_예외() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedJamsil.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");

    }

    @Test
    void 노선에_역이_존재할_때_새로운_역_2개를_추가하면_예외() {
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        Station savedYuksam = stationRepository.insert(YUKSAM_NO_ID);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedSeonleung.getId(), savedYuksam.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");

    }

    @Test
    void 하나의_구간만_존재할_때_해당_구간을_삭제하면_남은_역_2개_모두_삭제() {
        sectionService.delete(new LineStationDeleteRequest(savedGangnam.getId()));


        assertThatThrownBy(() -> stationRepository.findById(savedGangnam.getId()))
                .isInstanceOf(NotExistException.class)
                .hasMessage("존재하지 않는 stationId입니다.");

        assertThatThrownBy(() -> stationRepository.findById(savedJamsil.getId()))
                .isInstanceOf(NotExistException.class)
                .hasMessage("존재하지 않는 stationId입니다.");
    }

    @Test
    void 종점_제거() {
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        Section section = new Section(savedSeonleung, savedGangnam, new Distance(3));

        sectionRepository.insertSection(section, savedLine);

        sectionService.delete(new LineStationDeleteRequest(savedSeonleung.getId()));

        assertThatThrownBy(() -> stationRepository.findById(savedSeonleung.getId()))
                .isInstanceOf(NotExistException.class)
                .hasMessage("존재하지 않는 stationId입니다.");

        assertDoesNotThrow(() -> stationRepository.findById(savedGangnam.getId()));
    }

    @Test
    void 중간역_제거() {
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedSeonleung.getId(), 4, savedLine.getId());
        sectionService.insert(sectionCreateRequest);

        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(savedSeonleung.getId());
        sectionService.delete(lineStationDeleteRequest);

        assertAll(
                () -> assertThatThrownBy(() -> stationRepository.findById(savedSeonleung.getId()))
                        .isInstanceOf(NotExistException.class)
                        .hasMessage("존재하지 않는 stationId입니다."),
                () -> assertDoesNotThrow(() -> stationRepository.findById(savedGangnam.getId())),
                () -> assertDoesNotThrow(() -> stationRepository.findById(savedJamsil.getId()))
        );
    }

    @Test
    void 두_개의_노선에_걸치는_역_제거() {
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedSeonleung.getId(), 4, savedLine.getId());
        sectionService.insert(sectionCreateRequest);

        Line secondLine = lineRepository.insert(SECOND_LINE_NO_ID);
        Station savedSeokchon = stationRepository.insert(SEOKCHON_NO_ID);

        SectionCreateRequest seokchonJamsil = new SectionCreateRequest(savedJamsil.getId(), savedSeokchon.getId(), 15, secondLine.getId());
        sectionService.insert(seokchonJamsil);

        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(savedJamsil.getId());
        sectionService.delete(lineStationDeleteRequest);

        assertAll(
                () -> assertThatThrownBy(() -> stationRepository.findById(savedJamsil.getId()))
                        .isInstanceOf(NotExistException.class)
                        .hasMessage("존재하지 않는 stationId입니다."),
                () -> assertThatThrownBy(() -> stationRepository.findById(savedSeokchon.getId()))
                        .isInstanceOf(NotExistException.class)
                        .hasMessage("존재하지 않는 stationId입니다."),
                () -> assertThat(sectionRepository.isLastSectionInLine(savedLine)).isTrue()
        );
    }
}
