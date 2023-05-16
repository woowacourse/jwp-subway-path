package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class RoutedStations extends SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> {
    
    public RoutedStations(final Class<? extends DefaultWeightedEdge> edgeClass) {
        super(edgeClass);
    }
}
