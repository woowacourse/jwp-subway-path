package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.integration.TestFixture.GANGNAM;
import static subway.integration.TestFixture.JAMSIL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.section.Section;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.repository.LineRepository;
import subway.service.dto.SectionRequest;
import subway.service.dto.StationRequest;

@JdbcTest
@Import({LineService.class, LineDao.class, StationDao.class, SectionDao.class, StationService.class,
        SectionService.class, LineRepository.class})
class LineServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private LineRepository repository;

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void registerLine() {
        final long lineId = 1L;
        final SectionRequest sectionRequest =
                new SectionRequest(JAMSIL.getName(), GANGNAM.getName(), 10);

        lineService.registerStation(lineId, sectionRequest);

        final Line line = repository.findById(lineId);

        assertThat(line.getSections().getSections())
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .containsExactly(tuple(JAMSIL, GANGNAM, new Distance(10)));
    }

    @DisplayName("노선에 역을 삭제한다.")
    @Test
    void unregisterLine() {
        //given
        final long lineId = 1L;
        final SectionRequest sectionRequest =
                new SectionRequest(JAMSIL.getName(), GANGNAM.getName(), 10);
        lineService.registerStation(lineId, sectionRequest);
        final StationRequest stationRequest = new StationRequest(JAMSIL.getName());

        lineService.unregisterStation(lineId, stationRequest);

        final Line line = repository.findById(lineId);
        assertThat(line.getSections().getSections())
                .isEmpty();
    }
}
