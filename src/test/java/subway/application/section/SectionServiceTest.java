package subway.application.section;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.service.line.domain.Line;
import subway.service.section.SectionService;
import subway.service.section.dto.SectionCreateRequest;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.LineFixture.EIGHT_LINE_NO_ID;
import static subway.domain.StationFixture.GANGNAM;
import static subway.domain.StationFixture.JAMSIL;
import static subway.domain.StationFixture.SEONLEUNG;
import static subway.domain.StationFixture.YUKSAM;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class SectionServiceTest {

    @Autowired
    SectionService sectionService;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    StationDao stationDao;

    @Test
    void 존재하지_않는_노선에_역을_추가하려고_하면_예외() {

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 10, 20L);
        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    void 노선에_존재하는_역_2개를_추가하면_예외() {
        Line savedLine = lineDao.insert(EIGHT_LINE_NO_ID);
        Station savedJamsil = stationDao.insert(JAMSIL);
        Station savedGangnam = stationDao.insert(GANGNAM);
        SectionEntity sectionEntity = new SectionEntity(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가하려는 경로의 역들은 이미 노선에 존재하는 역들입니다.");

    }

    @Test
    void 동일한_역_2개를_추가하면_예외() {
        Line savedLine = lineDao.insert(EIGHT_LINE_NO_ID);
        Station savedJamsil = stationDao.insert(JAMSIL);
        Station savedGangnam = stationDao.insert(GANGNAM);
        SectionEntity sectionEntity = new SectionEntity(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedJamsil.getId(), savedJamsil.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");

    }

    @Test
    void 노선에_역이_존재할_때_새로운_역_2개를_추가하면_예외() {
        Line savedLine = lineDao.insert(EIGHT_LINE_NO_ID);
        Station savedJamsil = stationDao.insert(JAMSIL);
        Station savedGangnam = stationDao.insert(GANGNAM);
        SectionEntity sectionEntity = new SectionEntity(savedJamsil.getId(), savedGangnam.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station savedSeonleung = stationDao.insert(SEONLEUNG);
        Station savedYuksam = stationDao.insert(YUKSAM);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(savedSeonleung.getId(), savedYuksam.getId(), 10, savedLine.getId());

        assertThatThrownBy(() -> sectionService.insert(sectionCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");

    }

}
