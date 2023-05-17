package subway.persistance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import subway.Fixture;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.persistance.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@JdbcTest
@ContextConfiguration(classes = LineRepositoryImpl.class)
@Sql("/schema.sql")
class LineRepositoryImplTest {

    @Autowired
    private LineRepository lineRepository;
    @MockBean
    private StationDao stationDao;
    @MockBean
    private LineDao lineDao;
    @MockBean
    private SectionDao sectionDao;

    @Test
    @DisplayName("id로 라인을 조회한다")
    void findById() {
        // given
        when(lineDao.findById(1L)).thenReturn(Optional.of(Fixture.line1));
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(new SectionEntity(Fixture.sectionAB, 1L, 0),
                        new SectionEntity(Fixture.sectionBC, 1L, 1))
        );
        when(stationDao.findById(1L)).thenReturn(Optional.of(Fixture.stationA));
        when(stationDao.findById(2L)).thenReturn(Optional.of(Fixture.stationB));
        when(stationDao.findById(3L)).thenReturn(Optional.of(Fixture.stationC));

        // when
        final Line found = lineRepository.findById(1L).get();

        assertAll(
                () -> assertThat(found.getName()).isEqualTo(Fixture.line1.getName()),
                () -> assertThat(found.getColor()).isEqualTo(Fixture.line1.getColor()),
                () -> Assertions.assertThat(found.getSections()).containsAll(Fixture.line1.getSections())
        );
    }

    @Test
    @DisplayName("모든 라인을 조회한다")
    void findAll() {
        // given
        when(lineDao.insert(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(lineDao.findAll()).thenReturn(List.of(Fixture.line1, Fixture.line2, Fixture.line3));
        when(lineDao.findById(1L)).thenReturn(Optional.of(Fixture.line1));
        when(lineDao.findById(2L)).thenReturn(Optional.of(Fixture.line2));
        when(lineDao.findById(3L)).thenReturn(Optional.of(Fixture.line3));
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(new SectionEntity(Fixture.sectionAB, 1L, 0),
                        new SectionEntity(Fixture.sectionBC, 1L, 1))
        );
        when(sectionDao.findByLineId(2L)).thenReturn(
                List.of(new SectionEntity(Fixture.sectionAB, 2L, 0)
                ));
        when(sectionDao.findByLineId(3L)).thenReturn(
                List.of(new SectionEntity(Fixture.sectionBC, 3L, 1))
        );
        when(stationDao.findById(1L)).thenReturn(Optional.of(Fixture.stationA));
        when(stationDao.findById(2L)).thenReturn(Optional.of(Fixture.stationB));
        when(stationDao.findById(3L)).thenReturn(Optional.of(Fixture.stationC));

        // when
        final List<Line> lines = lineRepository.findAll().getLines();

        // then
        assertThat(lines.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("라인을 저장한다")
    void save() {
        // given
        when(lineDao.insert(any())).thenReturn(Fixture.line1);

        // when
        final Line saved = lineRepository.save(Fixture.line1);

        assertAll(
                () -> assertThat(saved.getName()).isEqualTo(Fixture.line1.getName()),
                () -> assertThat(saved.getColor()).isEqualTo(Fixture.line1.getColor()),
                () -> Assertions.assertThat(saved.getSections()).containsAll(Fixture.line1.getSections())
        );
    }

    @Test
    @DisplayName("id에 해당하는 라인을 삭제한다")
    void delete() {
        // when
        lineRepository.delete(Fixture.line1);

        // then
        verify(lineDao, times(1)).deleteById(1L);
        verify(sectionDao, times(1)).deleteByLineId(1L);
    }
}