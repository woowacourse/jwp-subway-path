package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.dao.StubSectionDao;
import subway.domain.section.Section;

class SectionDeleteServiceTest {

    private StubSectionDao stubSectionDao;
    private SectionDeleteService sectionDeleteService;

    @BeforeEach
    void setUp() {
        stubSectionDao = new StubSectionDao();
        sectionDeleteService = new SectionDeleteService(stubSectionDao);
    }

    @DisplayName("위 아래 모두 구간이 존재하지않으면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenNoSection() {
        assertThatThrownBy(() -> sectionDeleteService.deleteSection(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되어있지 않은 역은 지울 수 없습니다.");
    }

    @DisplayName("구간이 저장되어 있을 때")
    @Nested
    class DescribeSectionSaved {

        private long sectionId;

        @BeforeEach
        void setUp() {
            sectionId = stubSectionDao.insert(
                    Section.builder()
                            .lineId(1L)
                            .upStation(1L)
                            .downStation(2L)
                            .distance(4)
                            .build()
            ).getId();
        }

        @DisplayName("아랫쪽 구간만 존재하면 그 구간을 지운다.")
        @Test
        void deleteSectionWhenNoUpSection() {
            assertThatCode(() -> sectionDeleteService.deleteSection(1L, 1L))
                    .doesNotThrowAnyException();
            assertThat(stubSectionDao.findById(sectionId)).isEmpty();
        }

        @DisplayName("윗쪽 구간만 존재하면 그 구간을 지운다.")
        @Test
        void deleteSectionWhenNoDownSection() {
            assertThatCode(() -> sectionDeleteService.deleteSection(1L, 2L))
                    .doesNotThrowAnyException();
            assertThat(stubSectionDao.findById(sectionId)).isEmpty();
        }

        @DisplayName("윗 아래 모두 구간이 존재하면 두 구간을 합친다.")
        @Test
        void deleteSection() {
            final Long sectionId2 = stubSectionDao.insert(
                    Section.builder()
                            .lineId(1L)
                            .upStation(2L)
                            .downStation(3L)
                            .distance(5)
                            .build()
            ).getId();

            assertThatCode(() -> sectionDeleteService.deleteSection(1L, 2L))
                    .doesNotThrowAnyException();
            final List<Section> result = stubSectionDao.findByLineId(1L);

            assertAll(
                    () -> assertThat(stubSectionDao.findById(sectionId)).isEmpty(),
                    () -> assertThat(stubSectionDao.findById(sectionId2)).isEmpty(),
                    () -> assertThat(result).containsExactly(
                            Section.builder()
                                    .id(3L)
                                    .lineId(1L)
                                    .upStation(1L)
                                    .downStation(3L)
                                    .distance(9)
                                    .build()
                    )
            );
        }
    }
}
