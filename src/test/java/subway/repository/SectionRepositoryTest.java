package subway.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SectionRepositoryTest {

    @Mock
    SectionDao sectionDao;
    @InjectMocks
    SectionRepository sectionRepository;

    @DisplayName("라인에서 수정된 구간을 최신화한다")
    @Test
    void 라인에서_수정된_구간을_최신화한다() {
        //given
        Station station1 = new Station(1L, "잠실새내");
        Station station2 = new Station(2L,"잠실");
        Station station3 = new Station(3L, "잠실나루");
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(15);

        Section section1 = new Section(1L, station1, station2, distance1);
        Section section2 = new Section(2L, station2, station3, distance2);
        List<Section> sectionList = List.of(section1, section2);
        Sections sections = new Sections(sectionList);
        Line line2 = new Line(1L, new LineName("2호선"), new LineColor("초록"), sections);

        Section section3 = new Section(station1, station3, new Distance(25));
        List<Section> afterSectionList = List.of(section3);
        Sections afterSections = new Sections(afterSectionList);
        Line afterLine2 = new Line(1L, new LineName("2호선"), new LineColor("초록"), afterSections);
        //when
        sectionRepository.updateByLine(line2, afterLine2);
        //then
        Assertions.assertAll(
                () -> verify(sectionDao, times(1)).delete(List.of(
                        new SectionEntity(1L, 1L, 2L, 1L, 10),
                        new SectionEntity(2L, 2L, 3L, 1L, 15))),
                () -> verify(sectionDao, times(1)).save(List.of(
                        new SectionEntity(null, 1L, 3L, 1L, 25)))
        );
    }
}
