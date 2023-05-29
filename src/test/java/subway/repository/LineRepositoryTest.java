package subway.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.*;
import subway.exception.DuplicatedDataException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineRepositoryTest {

    @Mock
    StationDao stationDao;
    @Mock
    SectionDao sectionDao;
    @Mock
    LineDao lineDao;
    @InjectMocks
    LineRepository lineRepository;

    @DisplayName("저장한다")
    @Test
    void 저장한다() {
        //given
        Line line = new Line(new LineName("테스트이름"), new LineColor("테스트색"));
        when(lineDao.findByNameOrColor(new LineEntity(null, "테스트이름", "테스트색"))).thenReturn(Collections.EMPTY_LIST);
        when(lineDao.save(new LineEntity(null, "테스트이름", "테스트색"))).thenReturn(1L);
        //when
        Line save = lineRepository.save(line);
        //then
        assertThat(save).isEqualTo(new Line(1L, new LineName("테스트이름"), new LineColor("테스트색"), Sections.create()));
    }

    @DisplayName("이미 존재하는 이름이나 색은 예외를 발생한다")
    @Test
    void 이미_존재하는_이름은_예외를_발생한다() {
        //given
        Line line = new Line(new LineName("테스트이름"), new LineColor("테스트색"));
        when(lineDao.findByNameOrColor(new LineEntity(null, "테스트이름", "테스트색"))).thenReturn(List.of(new LineEntity(1L, "테스트이름", "테스트색")));
        //then
        assertThatThrownBy(() -> lineRepository.save(line)).isInstanceOf(DuplicatedDataException.class);
    }

    @DisplayName("아이디로 조회한다")
    @Test
    void 아이디로_조회한다() {
        //given
        when(stationDao.findAll()).thenReturn(List.of(
                new StationEntity(1L, "1번"),
                new StationEntity(2L, "2번"),
                new StationEntity(3L, "3번")
        ));
        when(sectionDao.findAll()).thenReturn(List.of(
                new SectionEntity(1L, 1L, 2L, 1L, 10),
                new SectionEntity(2L, 2L, 3L, 1L, 15)
        ));
        when(lineDao.findById(1L)).thenReturn(Optional.of(new LineEntity(1L, "테스트이름", "테스트색")));
        //when
        Line line = lineRepository.findById(1L);
        //then
        assertThat(line).isEqualTo(
                new Line(
                        1L,
                        new LineName("테스트이름"),
                        new LineColor("테스트색"),
                        new Sections(List.of(
                                new Section(
                                        1L,
                                        new Station(1L, "1번"),
                                        new Station(2L, "2번"),
                                        new Distance(10)
                                ),
                                new Section(
                                        2L,
                                        new Station(2L, "2번"),
                                        new Station(3L, "3번"),
                                        new Distance(15)
                                )
                        ))
                )
        );
    }

    @DisplayName("수정한다")
    @Test
    void 수정한다() {
        //given
        Line line = new Line(1L, new LineName("테스트명"), new LineColor("테스트색"), Sections.create());
        when(lineDao.findById(1L)).thenReturn(Optional.of(new LineEntity(1L, "전이름", "전색")));
        //when
        lineRepository.update(line);
        //then
        verify(lineDao, times(1)).update(new LineEntity(1L, "테스트명", "테스트색"));
    }

    @DisplayName("삭제한다")
    @Test
    void 삭제한다() {
        //given
        Line line = new Line(1L, new LineName("테스트명"), new LineColor("테스트색"), Sections.create());
        when(lineDao.findById(1L)).thenReturn(Optional.of(new LineEntity(1L, "테스트명", "테스트색")));
        //when
        lineRepository.delete(line);
        //then
        verify(lineDao).delete(new LineEntity(1L, "테스트명", "테스트색"));
    }
}
