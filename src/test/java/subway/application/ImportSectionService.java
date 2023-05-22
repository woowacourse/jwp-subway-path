package subway.application;

import org.springframework.context.annotation.Import;
import subway.application.strategy.delete.DeleteBetweenStation;
import subway.application.strategy.delete.DeleteDownTerminal;
import subway.application.strategy.delete.DeleteInitialSection;
import subway.application.strategy.delete.DeleteUpTerminal;
import subway.application.strategy.delete.SectionDeleter;
import subway.application.strategy.insert.InsertDownwardStation;
import subway.application.strategy.insert.InsertTerminal;
import subway.application.strategy.insert.InsertUpwardStation;
import subway.application.strategy.insert.SectionInserter;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SectionService.class, LineRepository.class, StationRepository.class, SectionRepository.class, SubwayReadService.class,
        SectionInserter.class, InsertUpwardStation.class, InsertDownwardStation.class, InsertTerminal.class,
        SectionDeleter.class, DeleteInitialSection.class, DeleteBetweenStation.class, DeleteUpTerminal.class, DeleteDownTerminal.class})
public @interface ImportSectionService {
}
