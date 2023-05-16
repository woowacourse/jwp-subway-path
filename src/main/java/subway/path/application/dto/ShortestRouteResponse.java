package subway.path.application.dto;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.Path;
import subway.path.domain.payment.PaymentLines;

public class ShortestRouteResponse {

    private final List<SectionInfo> sectionInfos;
    private final List<String> transferStations;
    private final int transferCount;
    private final int totalDistance;
    private final int totalFee;

    public ShortestRouteResponse(final List<SectionInfo> sectionInfos,
                                 final List<String> transferStations,
                                 final int totalDistance,
                                 final int totalFee) {
        this.sectionInfos = new ArrayList<>(sectionInfos);
        this.transferStations = new ArrayList<>(transferStations);
        this.totalDistance = totalDistance;
        this.transferCount = transferStations.size();
        this.totalFee = totalFee;
    }

    public static ShortestRouteResponse from(final PaymentLines paymentLines) {
        final Path path = paymentLines.lines();
        final List<SectionInfo> sectionInfoList = path.lines().stream()
                .flatMap(it -> SectionInfo.from(it).stream())
                .collect(toList());
        return new ShortestRouteResponse(
                sectionInfoList,
                toTransferStations(path),
                path.totalDistance(),
                paymentLines.calculateFee());
    }

    private static List<String> toTransferStations(final Path path) {
        return path.lines().stream()
                .map(it -> it.downTerminal().name())
                .limit(path.size() - 1)
                .collect(toList());
    }

    public List<SectionInfo> getSectionInfos() {
        return sectionInfos;
    }

    public List<String> getTransferStations() {
        return transferStations;
    }

    public int getTransferCount() {
        return transferCount;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public static class SectionInfo {
        private final String line;
        private final String fromStation;
        private final String toStation;
        private final int distance;

        public SectionInfo(final String line, final String fromStation, final String toStation, final int distance) {
            this.line = line;
            this.fromStation = fromStation;
            this.toStation = toStation;
            this.distance = distance;
        }

        public static SectionInfo of(final String line, final Section section) {
            return new SectionInfo(line, section.up().name(), section.down().name(), section.distance());
        }

        public static List<SectionInfo> from(final Line line) {
            return line.sections()
                    .stream()
                    .map(it -> SectionInfo.of(line.name(), it))
                    .collect(toList());
        }

        public String getLine() {
            return line;
        }

        public String getFromStation() {
            return fromStation;
        }

        public String getToStation() {
            return toStation;
        }

        public int getDistance() {
            return distance;
        }
    }
}
