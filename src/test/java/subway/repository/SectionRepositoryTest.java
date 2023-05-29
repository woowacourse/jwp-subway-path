package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.*;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SectionRepositoryTest {

    @Mock
    SectionDao sectionDao;
    @InjectMocks
    SectionRepository sectionRepository;

    @DisplayName("구간들을 저장한다")
    @Test
    void 라인에서_수정된_구간을_최신화한다() {
        //given
        Station station1 = new Station(1L, "잠실새내");
        Station station2 = new Station(2L, "잠실");
        Station station3 = new Station(3L, "잠실나루");
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(15);

        Section section1 = new Section(1L, station1, station2, distance1);
        Section section2 = new Section(2L, station2, station3, distance2);
        List<Section> sectionList = List.of(section1, section2);
        Sections sections = new Sections(sectionList);
        Line line = new Line(1L, new LineName("2호선"), new LineColor("초록"), sections);
        //when
        sectionRepository.saveAll(sectionList, line);
        //then
        verify(sectionDao, times(1)).save(List.of(
                        new SectionEntity(1L, 1L, 2L, 1L, 10),
                        new SectionEntity(2L, 2L, 3L, 1L, 15)));
    }

    @DisplayName("구간들을 삭제한다")
    @Test
    void 구간들을_삭제한다() {
        //given
        Station station1 = new Station(1L, "잠실새내");
        Station station2 = new Station(2L, "잠실");
        Station station3 = new Station(3L, "잠실나루");
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(15);

        Section section1 = new Section(1L, station1, station2, distance1);
        Section section2 = new Section(2L, station2, station3, distance2);
        List<Section> sectionList = List.of(section1, section2);
        Sections sections = new Sections(sectionList);
        Line line = new Line(1L, new LineName("2호선"), new LineColor("초록"), sections);
        //when
        sectionRepository.deleteAll(sectionList, line);
        //then
        verify(sectionDao, times(1)).delete(List.of(
                        new SectionEntity(1L, 1L, 2L, 1L, 10),
                        new SectionEntity(2L, 2L, 3L, 1L, 15)));
    }
}
