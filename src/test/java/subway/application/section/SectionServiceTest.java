package subway.application.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.persistence.dao.LineDaoImpl;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.service.line.domain.Line;
import subway.service.section.SectionService;
import subway.service.section.dto.SectionCreateRequest;
import subway.service.section.repository.SectionRepository;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.domain.LineFixture.EIGHT_LINE_NO_ID;
import static subway.domain.LineFixture.SECOND_LINE_NO_ID;
import static subway.domain.StationFixture.GANGNAM_NO_ID;
import static subway.domain.StationFixture.JAMSIL_NO_ID;
import static subway.domain.StationFixture.SEOKCHON_NO_ID;
import static subway.domain.StationFixture.SEONLEUNG_NO_ID;
import static subway.domain.StationFixture.YUKSAM_NO_ID;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@Sql("/test-schema.sql")
class SectionServiceTest {

    @Autowired
    SectionService sectionService;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    LineDaoImpl lineDao;

    @Autowired
    StationDao stationDao;

    Line savedLine;
    Station savedJamsil;
    Station savedGangnam;

    @BeforeEach
    void setUp() {
        savedLine = lineDao.insert(EIGHT_LINE_NO_ID);
        savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        savedGangnam = stationDao.insert(GANGNAM_NO_ID);
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());
        sectionService.insert(sectionCreateRequest);

        System.out.println(stationDao.findAll());
    }

    @Test
    void 존재하지_않는_노선에_역을_추가하려고_하면_예외() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 10, 20L);
        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
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
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        Station savedYuksam = stationDao.insert(YUKSAM_NO_ID);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedSeonleung.getId(), savedYuksam.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");

    }

    @Test
    void 하나의_구간만_존재할_때_해당_구간을_삭제하면_남은_역_2개_모두_삭제() {
        sectionService.delete(new LineStationDeleteRequest(savedGangnam.getId()));


        assertThatThrownBy(() -> stationDao.findById(savedGangnam.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 stationId입니다.");

        assertThatThrownBy(() -> stationDao.findById(savedJamsil.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 stationId입니다.");
    }

    @Test
    void 종점_제거() {
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        SectionEntity gangnamSeonleungEntity = new SectionEntity(savedSeonleung.getId(), savedGangnam.getId(), 3, savedLine.getId());

        sectionDao.insert(gangnamSeonleungEntity);

        sectionService.delete(new LineStationDeleteRequest(savedSeonleung.getId()));

        assertThatThrownBy(() -> stationDao.findById(savedSeonleung.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 stationId입니다.");

        assertDoesNotThrow(() -> stationDao.findById(savedGangnam.getId()));
    }

    @Test
    void 중간역_제거() {
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedSeonleung.getId(), 4, savedLine.getId());
        sectionService.insert(sectionCreateRequest);

        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(savedSeonleung.getId());
        sectionService.delete(lineStationDeleteRequest);

        assertAll(
                () -> assertThatThrownBy(() -> stationDao.findById(savedSeonleung.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 stationId입니다."),
                () -> assertDoesNotThrow(() -> stationDao.findById(savedGangnam.getId())),
                () -> assertDoesNotThrow(() -> stationDao.findById(savedJamsil.getId()))
        );
    }

    @Test
    void 두_개의_노선에_걸치는_역_제거() {
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedSeonleung.getId(), 4, savedLine.getId());
        sectionService.insert(sectionCreateRequest);

        Line secondLine = lineDao.insert(SECOND_LINE_NO_ID);
        Station savedSeokchon = stationDao.insert(SEOKCHON_NO_ID);

        SectionCreateRequest seokchonJamsil = new SectionCreateRequest(savedJamsil.getId(), savedSeokchon.getId(), 15, secondLine.getId());
        sectionService.insert(seokchonJamsil);

        LineStationDeleteRequest lineStationDeleteRequest = new LineStationDeleteRequest(savedJamsil.getId());
        sectionService.delete(lineStationDeleteRequest);


        assertAll(
                () -> assertThatThrownBy(() -> stationDao.findById(savedJamsil.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 stationId입니다."),
                () -> assertThatThrownBy(() -> stationDao.findById(savedSeokchon.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 stationId입니다."),
                () -> assertThat(sectionRepository.isLastSectionInLine(savedLine)).isTrue()
        );
    }
}
