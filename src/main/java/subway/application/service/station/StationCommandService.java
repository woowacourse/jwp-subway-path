package subway.application.service.station;

import subway.common.exception.NoSuchStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.station.CreateStationUseCase;
import subway.application.port.in.station.DeleteStationUseCase;
import subway.application.port.in.station.UpdateStationUseCase;
import subway.application.port.in.station.dto.command.CreateStationCommand;
import subway.application.port.in.station.dto.command.UpdateStationCommand;
import subway.application.port.out.station.LoadStationPort;
import subway.application.port.out.station.PersistStationPort;
import subway.domain.Station;

@Service
@Transactional
public class StationCommandService implements CreateStationUseCase, UpdateStationUseCase, DeleteStationUseCase {

    private final LoadStationPort loadStationPort;
    private final PersistStationPort persistStationPort;

    public StationCommandService(final LoadStationPort loadStationPort,
            final PersistStationPort persistStationPort) {
        this.loadStationPort = loadStationPort;
        this.persistStationPort = persistStationPort;
    }

    // TODO: 이름 중복 검사
    @Override
    public long createStation(final CreateStationCommand command) {
        return persistStationPort.create(new Station(command.getName()));
    }


    @Override
    public void updateStation(final UpdateStationCommand command) {
        validateStationId(command.getStationId());

        persistStationPort.update(new Station(command.getStationId(), command.getName()));
    }

    // TODO: Section의 해당 station 전부 지워주기
    @Override
    public void deleteStation(final long stationId) {
        validateStationId(stationId);

        persistStationPort.deleteById(stationId);
    }

    private void validateStationId(final long stationId) {
        loadStationPort.findById(stationId)
                .orElseThrow(NoSuchStationException::new);
    }
}
