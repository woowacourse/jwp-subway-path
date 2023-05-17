package subway.domain;

import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.entity.SectionEntity;

class LineEntitySectionsTest {
    
    private static final List<SectionEntity> SECTION_ENTITIES;
    
    static {
        SECTION_ENTITIES = List.of(
                new SectionEntity(1, 1, 2, 4),
                new SectionEntity(1, 1, 3, 1),
                new SectionEntity(1, 3, 4, 1),
                new SectionEntity(1, 4, 5, 1)
        );
    }
    
    
    @DisplayName("해당 라인의 구역이 비어있는지 확인한다.")
    @Test
    void isEmpty() {
        // given
        
        final LineSections lineSections = LineSections.from(Collections.emptyList());
        
        // when
        final boolean isEmpty = lineSections.isEmpty();
        
        // then
        Assertions.assertThat(isEmpty).isTrue();
    }
    
    @DisplayName("해당 라인에 역이 존재하는지 확인한다.")
    @Test
    void hasBaseStation() {
        // given
        final LineSections lineSections = LineSections.from(SECTION_ENTITIES);
        
        // when
        final boolean hasBaseStation = lineSections.hasStation(1);
        
        // then
        Assertions.assertThat(hasBaseStation).isTrue();
    }
    
    @DisplayName("해당 라인에 기준역이 종점인지 확인한다.")
    @Test
    void isTerminalStation() {
        // given
        final LineSections lineSections = LineSections.from(SECTION_ENTITIES);
        
        // when
        final boolean isTerminalStation = lineSections.isTerminalStation(2);
        
        // then
        Assertions.assertThat(isTerminalStation).isTrue();
    }
    
    
    @DisplayName("해당 라인에 상행 종점 아이디를 반환한다.")
    @Test
    void getUpTerminalStationId() {
        // given
        final LineSections lineSections = LineSections.from(SECTION_ENTITIES);
        
        // when
        final long upTerminalStationId = lineSections.getUpTerminalStationId();
        
        // then
        Assertions.assertThat(upTerminalStationId).isEqualTo(1);
    }
    
    @DisplayName("해당 라인에 하행 종점 아이디를 반환한다.")
    @Test
    void getDownTerminalStationId() {
        // given
        final LineSections lineSections = LineSections.from(SECTION_ENTITIES);
        
        // when
        final long downTerminalStationId = lineSections.getDownTerminalStationId();
        
        // then
        Assertions.assertThat(downTerminalStationId).isEqualTo(2);
    }
}