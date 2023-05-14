package subway.section.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import subway.section.dao.StubSectionDao;
import subway.section.entity.SectionEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class SectionEntityDeleteServiceTest {

    private StubSectionDao stubSectionDao;
    private SectionService sectionService;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setUp() {
        stubSectionDao = new StubSectionDao();
        sectionService = new SectionService(stubSectionDao, publisher);
    }

    @DisplayName("위 아래 모두 구간이 존재하지않으면 예외를 발생시킨다.")
    @Test
    void deleteSectionWhenNoSection() {
        assertThatThrownBy(() -> sectionService.deleteSection(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되어있지 않은 역은 지울 수 없습니다.");
    }

    @DisplayName("아랫쪽 구간만 존재하면 그 구간을 지운다.")
    @Test
    void deleteSectionWhenNoUpSection() {
        final SectionEntity saved = stubSectionDao.insert(new SectionEntity(1L, 2L, 3L, 4));
        final Long sectionId = saved.getId();
        sectionService.deleteSection(1L, 2L);
        final Optional<SectionEntity> result = stubSectionDao.findById(sectionId);
        Assertions.assertThat(result).isEmpty();
    }

    @DisplayName("윗쪽 구간만 존재하면 그 구간을 지운다.")
    @Test
    void deleteSectionWhenNoDownSection() {
        final SectionEntity saved = stubSectionDao.insert(new SectionEntity(1L, 2L, 3L, 4));
        final Long sectionId = saved.getId();
        sectionService.deleteSection(1L, 3L);
        final Optional<SectionEntity> result = stubSectionDao.findById(sectionId);
        Assertions.assertThat(result).isEmpty();
    }

    @DisplayName("윗 아래 모두 구간이 존재하면 두 구간을 합친다.")
    @Test
    void deleteSection() {
        stubSectionDao.insert(new SectionEntity(1L, 2L, 3L, 4));
        stubSectionDao.insert(new SectionEntity(1L, 3L, 4L, 5));

        sectionService.deleteSection(1L, 3L);

        final Optional<SectionEntity> section = stubSectionDao.findNeighborUpSection(1L, 4L);
        assertAll(
                () -> assertThat(section).isPresent(),
                () -> assertThat(section.get().getId()).isPositive(),
                () -> assertThat(section.get().getUpStationId()).isEqualTo(2L),
                () -> assertThat(section.get().getDownStationId()).isEqualTo(4L),
                () -> assertThat(section.get().getDistance()).isEqualTo(9)
        );
    }
}
