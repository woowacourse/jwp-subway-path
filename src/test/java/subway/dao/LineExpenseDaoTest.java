package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.dao.entity.LineExpenseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LineExpenseDaoTest extends DaoTestConfig {

    LineExpenseDao lineExpenseDao;

    @BeforeEach
    void setUp() {
        lineExpenseDao = new LineExpenseDao(jdbcTemplate);
    }

    @Test
    void 노선_추가_비용_정책을_저장한다() {
        // given
        final LineExpenseEntity 노선_추가_비용_정책_엔티티 = new LineExpenseEntity(1L, "1000", 5, 1L);

        // when
        final Long saveId = lineExpenseDao.save(노선_추가_비용_정책_엔티티);

        // then
        assertThat(saveId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 노선_식별자값을_통해_노선_추가_비용_정책을_조회한다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선_식별자값);
        final Long 파랑_노선_추가_비용_정책_식별자값 = lineExpenseDao.save(파랑_노선_추가_비용_정책_엔티티);

        // when
        final Optional<LineExpenseEntity> 노선_추가_비용_정책_엔티티 = lineExpenseDao.findByLineId(파랑_노선_식별자값);

        // then
        assertThat(노선_추가_비용_정책_엔티티)
                .contains(new LineExpenseEntity(파랑_노선_추가_비용_정책_식별자값, "1000", 5, 파랑_노선_식별자값));
    }

    @Test
    void 전체_노선_추가_비용_정책을_조회한다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        final Long 초록_노선_식별자값 = 2L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선_식별자값);
        final LineExpenseEntity 초록_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("2000", 10, 초록_노선_식별자값);

        final Long 파랑_노선_추가_비용_정책_식별자값 = lineExpenseDao.save(파랑_노선_추가_비용_정책_엔티티);
        final Long 초록_노선_추가_비용_정책_식별자값 = lineExpenseDao.save(초록_노선_추가_비용_정책_엔티티);

        // when
        final List<LineExpenseEntity> 노선_추가_비용_정책_목록 = lineExpenseDao.findAll();

        // then
        assertThat(노선_추가_비용_정책_목록)
                .contains(
                        new LineExpenseEntity(파랑_노선_추가_비용_정책_식별자값, "1000", 5, 파랑_노선_식별자값),
                        new LineExpenseEntity(초록_노선_추가_비용_정책_식별자값, "2000", 10, 초록_노선_식별자값)
                );
    }

    @Test
    void 노선_추가_비용_정책을_수정한다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선_식별자값);
        final Long 파랑_노선_추가_비용_정책_식별자값 = lineExpenseDao.save(파랑_노선_추가_비용_정책_엔티티);

        // when
        final LineExpenseEntity 파랑_노선_추가_비용_정책_수정_엔티티 = new LineExpenseEntity("2000", 10, 파랑_노선_식별자값);
        lineExpenseDao.update(파랑_노선_추가_비용_정책_수정_엔티티);

        final Optional<LineExpenseEntity> 수정된_파랑_노선_추가_비용_정책_엔티티 = lineExpenseDao.findByLineId(파랑_노선_식별자값);

        // then
        assertThat(수정된_파랑_노선_추가_비용_정책_엔티티)
                .contains(new LineExpenseEntity(파랑_노선_추가_비용_정책_식별자값, "2000", 10, 파랑_노선_식별자값));
    }
}
