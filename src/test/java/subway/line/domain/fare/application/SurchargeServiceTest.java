package subway.line.domain.fare.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.domain.fare.Fare;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.station.Station;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class SurchargeServiceTest {
    private SurchargeService surchargeService;
    SectionService sectionService = mock(SectionService.class);
    SurchargeRepository surchargeRepository = mock(SurchargeRepository.class);


    @BeforeEach
    void setup() {
        surchargeService = new SurchargeService(sectionService, surchargeRepository);
    }

    @Test
    @DisplayName("주어진 역들이 지나는 노선들의 추가부과요금 리스트를 반환한다.")
    void surcharge() {
        final var 잠실역 = new Station(1L, "잠실역");
        final var 잠실새내역 = new Station(2L, "잠실새내역");
        final var 종합운동장 = new Station(3L, "종합운동장");
        final var 삼성역 = new Station(4L, "삼성역");
        final var 이어폰역 = new Station(5L, "이어폰역");

        final var 최단경로들 = List.of(잠실역, 잠실새내역, 종합운동장, 삼성역, 이어폰역);

        final var 이호선추가요금 = new Fare(new BigDecimal("100"));
        final var 신림선추가요금 = new Fare(new BigDecimal("500"));

        when(sectionService.findLineIdBySectionHavingStations(잠실역, 잠실새내역)).thenReturn(2L);
        when(sectionService.findLineIdBySectionHavingStations(잠실새내역, 종합운동장)).thenReturn(3L);
        when(sectionService.findLineIdBySectionHavingStations(종합운동장, 삼성역)).thenReturn(4L);
        when(sectionService.findLineIdBySectionHavingStations(삼성역, 이어폰역)).thenReturn(5L);

        when(surchargeRepository.hasLineSurcharge(2L)).thenReturn(false);
        when(surchargeRepository.hasLineSurcharge(3L)).thenReturn(true);
        when(surchargeRepository.hasLineSurcharge(4L)).thenReturn(false);
        when(surchargeRepository.hasLineSurcharge(5L)).thenReturn(true);

        when(surchargeRepository.findByLineId(3L)).thenReturn(이호선추가요금);
        when(surchargeRepository.findByLineId(5L)).thenReturn(신림선추가요금);

        Assertions
                .assertThat(surchargeService.getSurcharges(최단경로들))
                .contains(이호선추가요금, 신림선추가요금);

        verify(sectionService, atLeastOnce()).findLineIdBySectionHavingStations(잠실역, 잠실새내역);
        verify(sectionService, atLeastOnce()).findLineIdBySectionHavingStations(잠실새내역, 종합운동장);
        verify(sectionService, atLeastOnce()).findLineIdBySectionHavingStations(종합운동장, 삼성역);
        verify(sectionService, atLeastOnce()).findLineIdBySectionHavingStations(삼성역, 이어폰역);
    }
}