package subway.line.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.line.dao.SectionDao;
import subway.line.dao.SectionEntity;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.line.domain.Stations;
import subway.line.exception.CanNotDuplicatedSectionException;

class SectionCommandServiceTest extends IntegrationTestHelper {

  @Autowired
  private SectionCommandService sectionCommandService;

  @Autowired
  private SectionDao sectionDao;

  @Test
  @DisplayName("registerSection() : 섹션을 저장할 수 있다.")
  void test_registerSection() throws Exception {
    //given
    final String currentStationName = "D";
    final String nextStationName = "Z";
    final Long lineId = 2L;
    final int distance = 4;

    final int beforeSize = sectionDao.findSectionsByLineId(lineId).size();

    //when
    sectionCommandService.registerSection(currentStationName, nextStationName, distance, lineId);

    //then
    final int afterSize = sectionDao.findSectionsByLineId(lineId).size();

    assertEquals(afterSize, beforeSize + 1);
  }

  @Test
  @DisplayName("registerSection() : 섹션을 저장할 때, 해당 호선에 같은 섹션이 있으면 CanNotDuplicatedSectionException이 발생한다.")
  void test_registerSection_CanNotDuplicatedSectionException() throws Exception {
    //given
    final String currentStationName = "B";
    final String nextStationName = "F";
    final Long lineId = 2L;
    final int distance = 4;

    //when & then
    assertThatThrownBy(
        () -> sectionCommandService.registerSection(currentStationName, nextStationName, distance,
            lineId))
        .isInstanceOf(CanNotDuplicatedSectionException.class);
  }

  @Test
  @DisplayName("deleteAll() : 주어진 호선에 있는 섹션들을 모두 삭제할 수 있다.")
  void test_deleteAll() throws Exception {
    //given
    final long lineId = 1L;

    //when
    sectionCommandService.deleteAll(lineId);

    //then
    assertEquals(0, sectionDao.findSectionsByLineId(lineId).size());
  }

  @Test
  @DisplayName("deleteSection() : section id를 통해 섹션을 삭제할 수 있다.")
  void test_deleteSection() throws Exception {
    //given
    final long sectionId = 1L;
    final long lineId = 1L;

    final int beforeSize = sectionDao.findSectionsByLineId(lineId).size();

    //when
    sectionCommandService.deleteSectionById(sectionId);

    //then
    final int afterSize = sectionDao.findSectionsByLineId(lineId).size();

    assertEquals(afterSize, beforeSize - 1);
  }

  @Test
  @DisplayName("updateSection() : 해당 섹션을 수정할 수 있다.")
  void test_updateSection() throws Exception {
    //given
    final Stations stations = new Stations(new Station("Z"), new Station("V"), 5);
    final long sectionId = 3L;
    final Section section = new Section(sectionId, stations);

    //when
    sectionCommandService.updateSection(section);

    //then
    final SectionEntity updatedSection =
        sectionDao.findSectionsByLineId(1L)
            .stream()
            .filter(it -> it.getId().equals(sectionId))
            .findAny()
            .orElseThrow();

    assertAll(
        () -> assertEquals(updatedSection.getNextStationName(),
            section.getStations().getNext().getName()),
        () -> assertEquals(updatedSection.getCurrentStationName(),
            section.getStations().getCurrent().getName()),
        () -> assertEquals(updatedSection.getDistance(), section.getStations().getDistance()),
        () -> assertEquals(updatedSection.getId(), section.getId())
    );
  }
}
