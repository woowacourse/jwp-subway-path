package subway.application.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.section.AddStationToLineUseCase;
import subway.application.port.in.section.RemoveStationFromLineUseCase;
import subway.application.port.in.section.dto.command.AddStationToLineCommand;
import subway.application.port.in.section.dto.command.RemoveStationFromLineCommand;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.line.PersistLinePort;
import subway.application.port.out.station.LoadStationPort;
import subway.common.exception.NoSuchLineException;
import subway.common.exception.NoSuchStationException;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

@Service
@Transactional
public class SectionCommandService implements AddStationToLineUseCase, RemoveStationFromLineUseCase {

    private final LoadLinePort loadLinePort;
    private final PersistLinePort persistLinePort;
    private final LoadStationPort loadStationPort;

    public SectionCommandService(final LoadLinePort loadLinePort,
            final PersistLinePort persistLinePort, final LoadStationPort loadStationPort) {
        this.loadLinePort = loadLinePort;
        this.persistLinePort = persistLinePort;
        this.loadStationPort = loadStationPort;
    }

    @Override
    public void addStation(final AddStationToLineCommand command) {
        Line line = loadLinePort.findById(command.getLineId())
                .orElseThrow(NoSuchLineException::new);

        Station upStation = loadStationPort.findById(command.getUpStationId())
                .orElseThrow(NoSuchStationException::new);
        Station downStation = loadStationPort.findById(command.getDownStationId())
                .orElseThrow(NoSuchStationException::new);

        line.addSection(new Section(upStation, downStation, command.getDistance()));

        persistLinePort.updateSections(line);
    }

    @Override
    public void removeStation(final RemoveStationFromLineCommand command) {
        Line line = loadLinePort.findById(command.getLineId())
                .orElseThrow(NoSuchLineException::new);

        Station station = loadStationPort.findById(command.getStationId())
                .orElseThrow(NoSuchStationException::new);

        line.removeStation(station);

        persistLinePort.updateSections(line);
    }
}
