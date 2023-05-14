package subway.ui.dto;

//@Getter
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class LineResponse {
//
//    private Long id;
//    private String name;
//    private String color;
//    private List<StationResponse> stations;
//
//    public static LineResponse from(final Line line) {
//        final List<StationResponse> stationResponses = line.getInterStations()
//            .getInterStations()
//            .stream()
//            .map(interStation -> interStation.getUpStationId())
//            .map(StationResponse::from)
//            .collect(Collectors.toList());
//        return new LineResponse(line.getId(), line.getName().getValue(), line.getColor().getValue(), stationResponses);
//    }
//}
