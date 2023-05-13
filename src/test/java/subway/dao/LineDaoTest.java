package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.dto.LineWithSection;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;

@JdbcTest
@Import({LineDao.class, SectionDao.class, StationDao.class})
public class LineDaoTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @Test
    @DisplayName("노선 정보를 저장한다")
    void insert() {
        // when
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        final Long 저장된_이호선_아이디 = lineDao.insert(이호선_엔티티);

        // then
        final Optional<LineEntity> line = lineDao.findById(저장된_이호선_아이디);
        final LineEntity findLine = line.get();
        assertThat(findLine)
            .extracting(LineEntity::getName, LineEntity::getColor)
            .containsExactly("이호선", "bg-green-600");
    }

    @Test
    @DisplayName("유효한 노선 아이디가 주어지면, 노선 정보를 조회한다")
    void findById_success() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        final Long 저장된_이호선_아이디 = lineDao.insert(이호선_엔티티);

        // when
        final Optional<LineEntity> line = lineDao.findById(저장된_이호선_아이디);

        // then
        final LineEntity findLine = line.get();
        assertThat(findLine)
            .extracting(LineEntity::getName, LineEntity::getColor)
            .containsExactly("이호선", "bg-green-600");
    }

    @DisplayName("유효하지 않은 노선 아이디가 주어지면, 빈 값을 반환한다")
    @Test
    void findById_empty() {
        // when
        final Optional<LineEntity> findLine = lineDao.findById(1L);

        // then
        assertThat(findLine).isEmpty();
    }

    @Test
    @DisplayName("특정 노선에 존재하는 구간 정보를 조회한다.")
    void findByLindIdWithSections() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);

        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final StationEntity 선릉역_엔티티 = new StationEntity("선릉역");
        final StationEntity 강남역_엔티티 = new StationEntity("강남역");
        final Long 저장된_잠실역_엔티티_아이디 = stationDao.insert(잠실역_엔티티);
        final Long 저장된_선릉역_엔티티_아이디 = stationDao.insert(선릉역_엔티티);
        final Long 저장된_강남역_엔티티_아이디 = stationDao.insert(강남역_엔티티);

        final SectionEntity 잠실_선릉_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 10);
        final SectionEntity 선릉_강남_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 저장된_강남역_엔티티_아이디, 10);
        sectionDao.insert(잠실_선릉_엔티티);
        sectionDao.insert(선릉_강남_엔티티);

        // when
        final List<LineWithSection> lines = lineDao.findByLindIdWithSections(저장된_이호선_엔티티_아이디);

        // then
        assertThat(lines).hasSize(2);
        assertThat(lines)
            .extracting(LineWithSection::getLineId, LineWithSection::getLineName, LineWithSection::getLineColor,
                LineWithSection::getSourceStationId, LineWithSection::getSourceStationName,
                LineWithSection::getTargetStationId,
                LineWithSection::getTargetStationName, LineWithSection::getDistance)
            .containsExactly(
                tuple(저장된_이호선_엔티티_아이디, "이호선", "bg-green-600", 저장된_잠실역_엔티티_아이디, "잠실역",
                    저장된_선릉역_엔티티_아이디, "선릉역", 10),
                tuple(저장된_이호선_엔티티_아이디, "이호선", "bg-green-600", 저장된_선릉역_엔티티_아이디, "선릉역",
                    저장된_강남역_엔티티_아이디, "강남역", 10));
    }

    @Test
    @DisplayName("모든 노선에 존재하는 구간 정보를 조회한다.")
    void findAllWithSections() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        final LineEntity 팔호선_엔티티 = new LineEntity("팔호선", "bg-pink-600");
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);
        final Long 저장된_팔호선_엔티티_아이디 = lineDao.insert(팔호선_엔티티);

        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final StationEntity 선릉역_엔티티 = new StationEntity("선릉역");
        final StationEntity 강남역_엔티티 = new StationEntity("강남역");
        final Long 저장된_잠실역_엔티티_아이디 = stationDao.insert(잠실역_엔티티);
        final Long 저장된_선릉역_엔티티_아이디 = stationDao.insert(선릉역_엔티티);
        final Long 저장된_강남역_엔티티_아이디 = stationDao.insert(강남역_엔티티);

        final StationEntity 복정역_엔티티 = new StationEntity("복정역");
        final StationEntity 남위례역_엔티티 = new StationEntity("남위례역");
        final StationEntity 산성역_엔티티 = new StationEntity("산성역");
        final Long 복정역_엔티티_엔티티_아이디 = stationDao.insert(복정역_엔티티);
        final Long 남위례역_엔티티_엔티티_아이디 = stationDao.insert(남위례역_엔티티);
        final Long 산성역_엔티티_엔티티_아이디 = stationDao.insert(산성역_엔티티);

        final SectionEntity 잠실_선릉_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 10);
        final SectionEntity 선릉_강남_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 저장된_강남역_엔티티_아이디, 10);
        final SectionEntity 복정_남위례_엔티티 = new SectionEntity(저장된_팔호선_엔티티_아이디, 복정역_엔티티_엔티티_아이디, 남위례역_엔티티_엔티티_아이디, 10);
        final SectionEntity 남위례_산성_엔티티 = new SectionEntity(저장된_팔호선_엔티티_아이디, 남위례역_엔티티_엔티티_아이디, 산성역_엔티티_엔티티_아이디, 10);
        sectionDao.insert(잠실_선릉_엔티티);
        sectionDao.insert(선릉_강남_엔티티);
        sectionDao.insert(복정_남위례_엔티티);
        sectionDao.insert(남위례_산성_엔티티);

        // when
        final List<LineWithSection> lines = lineDao.findAllWithSections();

        // then
        assertThat(lines).hasSize(4);
        assertThat(lines)
            .extracting(LineWithSection::getLineId, LineWithSection::getLineName, LineWithSection::getLineColor,
                LineWithSection::getSourceStationId, LineWithSection::getSourceStationName,
                LineWithSection::getTargetStationId,
                LineWithSection::getTargetStationName, LineWithSection::getDistance)
            .containsExactly(
                tuple(저장된_이호선_엔티티_아이디, "이호선", "bg-green-600", 저장된_잠실역_엔티티_아이디, "잠실역",
                    저장된_선릉역_엔티티_아이디, "선릉역", 10),
                tuple(저장된_이호선_엔티티_아이디, "이호선", "bg-green-600", 저장된_선릉역_엔티티_아이디, "선릉역",
                    저장된_강남역_엔티티_아이디, "강남역", 10),
                tuple(저장된_팔호선_엔티티_아이디, "팔호선", "bg-pink-600", 복정역_엔티티_엔티티_아이디, "복정역",
                    남위례역_엔티티_엔티티_아이디, "남위례역", 10),
                tuple(저장된_팔호선_엔티티_아이디, "팔호선", "bg-pink-600", 남위례역_엔티티_엔티티_아이디, "남위례역",
                    산성역_엔티티_엔티티_아이디, "산성역", 10));
    }

    @Test
    @DisplayName("주어진 노선 아이디에 해당하는 노선 정보를 수정한다.")
    void updateById() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);

        // when
        final LineEntity 수정_요청_엔티티 = new LineEntity(저장된_이호선_엔티티_아이디, "일호선", "bg-blue-600");
        final int updatedCount = lineDao.update(수정_요청_엔티티);

        // then
        final Optional<LineEntity> line = lineDao.findById(저장된_이호선_엔티티_아이디);
        final LineEntity findLine = line.get();

        assertThat(updatedCount).isSameAs(1);
        assertThat(findLine)
            .extracting(LineEntity::getName, LineEntity::getColor)
            .containsExactly("일호선", "bg-blue-600");
    }

    @Test
    @DisplayName("주어진 노선 아이디에 해당하는 노선을 삭제한다.")
    void deleteById() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);

        // when
        final int deletedCount = lineDao.deleteById(저장된_이호선_엔티티_아이디);

        // then
        final Optional<LineEntity> lineEntity = lineDao.findById(저장된_이호선_엔티티_아이디);

        assertThat(lineEntity).isEmpty();
        assertThat(deletedCount).isEqualTo(1);
    }

    @ParameterizedTest(name = "주어진 이름을 가진 노선이 존재하면 true를, 아니면 false를 반환한다.")
    @CsvSource(value = {"이호선:true", "팔호선:false"}, delimiter = ':')
    void existByName(final String name, final boolean expected) {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-green-600");
        lineDao.insert(이호선_엔티티);

        // expected
        assertThat(lineDao.existByName(name))
            .isSameAs(expected);
    }
}
