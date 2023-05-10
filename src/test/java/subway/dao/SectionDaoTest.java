package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/test-data.sql"})
@Import(SectionDao.class)
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("section 테이블에 값을 추가한다.")
    @Test
    void insert() {
        // given
        SectionRequest sectionRequest = new SectionRequest(2L, 1L, 1L, 1);
        Section section = new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), new Distance(sectionRequest.getDistance()));

        // when
        Long sectionId = sectionDao.insert(section);

        // then
        assertThat(sectionId).isEqualTo(1L);
    }

    @DisplayName("section 테이블에서 stationId에 해당하는 값을 삭제한다.")
    @Test
    void deleteByStationId() {
        // given
        Long stationId = 2L;
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(stationId, lineId, 1L, 1);
        Section section = new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), new Distance(sectionRequest.getDistance()));
        Long sectionId = sectionDao.insert(section);

        // when
        sectionDao.deleteByLineIdAndStationId(lineId, stationId);

        // then
        assertThat(sectionDao.findByLineIdAndStationId(lineId, stationId).isEmpty()).isTrue();
    }
}
