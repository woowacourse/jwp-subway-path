package subway.service.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class RouteMapInSubway {

    private final Map<Station, List<Path>> map;

    private RouteMapInSubway(Map<Station, List<Path>> map) {
        this.map = map;
    }

    public static RouteMapInSubway from(List<Map<Station, List<Path>>> maps) {
        Map<Station, List<Path>> mergeMap = new HashMap<>();
        maps.stream()
                .flatMap(map -> map.entrySet().stream())
                .forEach(stationPathEntry -> mergePaths(mergeMap, stationPathEntry));
        return new RouteMapInSubway(mergeMap);
    }

    private static void mergePaths(Map<Station, List<Path>> mergeMap, Map.Entry<Station, List<Path>> stationPathEntry) {
        mergeMap.computeIfAbsent(stationPathEntry.getKey(), ignored -> new ArrayList<>());
        mergeMap.get(stationPathEntry.getKey()).addAll(stationPathEntry.getValue());
    }

    public ShortestPath getShortestPath(Station source,
                                        Station destination,
                                        FarePolicies farePolicy,
                                        Age age) {
        PriorityQueue<PathElement> stationsInPath = new PriorityQueue<>(this::comparePathElement);
        Map<Station, StationRouteInfo> stationRouteMap = new HashMap<>();
        initDijkstra(source, stationsInPath, stationRouteMap);
        dijkstra(destination, stationsInPath, stationRouteMap);
        isShortestPathNotFound(destination, stationRouteMap);
        ShortestPathInfo shortestPathInfo = pathTracking(destination, source, stationRouteMap);
        return new ShortestPath(shortestPathInfo, Fare.of(shortestPathInfo, farePolicy, age));
    }

    private Integer comparePathElement(PathElement firstElement, PathElement secondElement) {
        return firstElement.getDistance() - secondElement.getDistance();
    }

    private void initDijkstra(Station start, PriorityQueue<PathElement> stationsInPath, Map<Station, StationRouteInfo> stationRouteMap) {
        stationsInPath.add(new PathElement(start, 0));
        map.forEach((key, value) ->
                stationRouteMap.put(key, new StationRouteInfo(Integer.MAX_VALUE))
        );
        stationRouteMap.put(start, new StationRouteInfo(0));
    }

    private void dijkstra(Station destination,
                          PriorityQueue<PathElement> stationsInPath,
                          Map<Station, StationRouteInfo> stationRouteMap) {
        boolean isArrivedDestination = false;
        while (!stationsInPath.isEmpty() && !isArrivedDestination) {
            PathElement pathElement = stationsInPath.poll();
            Station nowStation = pathElement.getStation();
            Integer nowDistance = pathElement.getDistance();
            isArrivedDestination = nowStation.equals(destination);
            searchConnectedStation(stationsInPath, stationRouteMap, isArrivedDestination, nowStation, nowDistance);
        }
    }

    private void searchConnectedStation(PriorityQueue<PathElement> stationsInPath,
                                        Map<Station, StationRouteInfo> stationRouteMap,
                                        boolean isArrivedDestination,
                                        Station nowStation,
                                        Integer nowDistance) {
        if (isNotIncludedNowStationInShortestPath(stationRouteMap, isArrivedDestination, nowStation, nowDistance)) {
            searchConnectedNowStation(stationsInPath, stationRouteMap, nowStation, nowDistance);
        }
    }

    private boolean isNotIncludedNowStationInShortestPath(Map<Station, StationRouteInfo> stationRouteMap, boolean isArrivedDestination, Station nowStation, Integer nowDistance) {
        return !isArrivedDestination
                && stationRouteMap.get(nowStation).getDistance() >= nowDistance;
    }

    private void searchConnectedNowStation(PriorityQueue<PathElement> stationsInPath,
                                           Map<Station, StationRouteInfo> stationRouteMap,
                                           Station nowStation,
                                           Integer nowDistance) {
        for (Path path : map.get(nowStation)) {
            StationRouteInfo stationRouteInfo = stationRouteMap.get(path.getNextStation());
            int newDistance = nowDistance + path.getDistance();
            findNextStationInShortestPath(stationsInPath, stationRouteMap, nowStation, path, stationRouteInfo, newDistance);
        }
    }

    private void findNextStationInShortestPath(PriorityQueue<PathElement> stationsInPath,
                                               Map<Station, StationRouteInfo> stationRouteMap,
                                               Station nowStation,
                                               Path path,
                                               StationRouteInfo stationRouteInfo,
                                               int newDistance) {
        if (newDistance < stationRouteInfo.getDistance()) {
            stationsInPath.add(new PathElement(path.getNextStation(), newDistance));
            stationRouteMap.put(
                    path.getNextStation(),
                    new StationRouteInfo(nowStation, path.getLineProperty(), newDistance)
            );
        }
    }

    private void isShortestPathNotFound(Station end, Map<Station, StationRouteInfo> stationRouteMap) {
        Integer totalDistance = stationRouteMap.get(end).getDistance();

        if (totalDistance == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("최단 경로를 찾을 수 없습니다");
        }
    }

    public ShortestPathInfo pathTracking(Station source,
                                         Station destination,
                                         Map<Station, StationRouteInfo> stationRouteMap) {
        List<Station> stationsInLine = new ArrayList<>();
        Set<LineProperty> usedLines = new HashSet<>();
        track(source, destination, stationRouteMap, stationsInLine, usedLines);
        Collections.reverse(stationsInLine);
        return new ShortestPathInfo(stationRouteMap.get(source).getDistance(), usedLines, new Stations(stationsInLine));
    }

    private void track(Station source, Station destination, Map<Station, StationRouteInfo> stationRouteMap, List<Station> stationsInLine, Set<LineProperty> usedLines) {
        stationsInLine.add(source);
        usedLines.add(stationRouteMap.get(source).getUsedLine());

        while (!getCurrentStation(stationsInLine).equals(destination)) {
            StationRouteInfo stationRouteInfo = stationRouteMap.get(getCurrentStation(stationsInLine));
            stationsInLine.add(stationRouteInfo.getPreviousStation());
            usedLines.add(stationRouteInfo.getUsedLine());
        }
    }

    private Station getCurrentStation(List<Station> stationsInLine) {
        return stationsInLine.get(stationsInLine.size() - 1);
    }

}

