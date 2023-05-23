package subway.line.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.line.UnRegisteredLine;
import subway.line.application.LineService;
import subway.line.domain.fare.infrastructure.SurchargeDao;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SurchargeDaoTest {
    @Autowired
    private SurchargeDao surchargeDao;

    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("특정 노선 아이디에 대한 추가요금을 저장한다")
    void save() {
        final var line = lineService.saveLine(new UnRegisteredLine("1호선", "yellow"));
        final var surcharge = surchargeDao.save(line.getId(), new BigDecimal("900"));
        assertThat(surcharge.getMoney()).isEqualTo(new BigDecimal("900"));
    }

    @Test
    @DisplayName("특정 노선 아이디에 대한 추가요금을 조회한다")
    void find() {
        final var line = lineService.saveLine(new UnRegisteredLine("1호선", "yellow"));
        surchargeDao.save(line.getId(), new BigDecimal("900"));
        final var surcharge = surchargeDao.findByLineId(line.getId());
        assertThat(surcharge.getMoney()).isEqualTo(new BigDecimal("900"));
    }
}