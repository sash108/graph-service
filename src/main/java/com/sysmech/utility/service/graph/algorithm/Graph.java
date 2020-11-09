package com.sysmech.utility.service.graph.algorithm;

import com.sysmech.utility.service.graph.model.NodeEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * It is core logic of handling the graph, which is responsible to add nodes in the graph,
 * assign association between nodes
 * and finding possible shortest path between any given nodes.
 */
@Slf4j
@Getter
public class Graph {

    private final Map<String, NodeEntity> allNodeEntities;

    public Graph() {
        allNodeEntities = new HashMap<>();
    }

    /**
     * @param key       Key attribute of node entity, which is going to be added in the graph.
     * @param classType Classification type of node entity, which is going to be added in the graph.
     */
    public void addEntity(String key, String classType) {
        if (key != null && classType != null
                && !key.isBlank() && !classType.isBlank()) {
            allNodeEntities.putIfAbsent( key, new NodeEntity( key, classType, null ) );
        }
    }

    /**
     * @param primaryEntityKey   The source entity node in the graph to start direction/connection from
     * @param secondaryEntityKey The destination entity node in the graph to end direction/connection to
     */
    public void assignAssociation(String primaryEntityKey, String secondaryEntityKey) {

        if (allNodeEntities.containsKey( primaryEntityKey )
                && allNodeEntities.containsKey( secondaryEntityKey ))
            allNodeEntities.get( primaryEntityKey ).addNeighbor( allNodeEntities.get( secondaryEntityKey ) );
        else
            log.info( "One of Entity does not exist. There must be valid entity before making association between them" );
    }


    /**
     * @param sourceEntityKey      Starting point in the graph to start finding path
     * @param destinationEntityKey Ending point in the graph to finish finding path
     * @return It returns the list of all node entities which are being traversed between provided entities
     * otherwise null if no path found between these entities.
     */
    public Optional<List<NodeEntity>> findShortestPath(String sourceEntityKey, String destinationEntityKey) {

        if (!(allNodeEntities.containsKey( sourceEntityKey )
                && allNodeEntities.containsKey( destinationEntityKey )))
            return Optional.empty();

        // distance of each node entity from source node entity
        Map<String, Integer> distance = new HashMap<>();
        // for every given node entity, map to track previous node entity which gave the distance
        Map<String, String> prevNodeHop = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        // initialization
        allNodeEntities.keySet().forEach( key -> distance.put( key, -1 ) );
        allNodeEntities.keySet().forEach( key -> prevNodeHop.put( key, null ) );
        distance.put( sourceEntityKey, 0 );
        queue.add( sourceEntityKey );

        while (!queue.isEmpty()) {

            String currentEntity = queue.remove();
            final List<NodeEntity> neighborEntities = allNodeEntities.get( currentEntity ).getNeighborEntities();

            if (neighborEntities != null) {

                neighborEntities.forEach(
                        neighborEntity -> {
                            String neighborEntityKey = neighborEntity.getKey();
                            if (distance.get( neighborEntityKey ) == -1) {

                                distance.put( neighborEntityKey, distance.get( currentEntity ) + 1 );
                                prevNodeHop.put( neighborEntityKey, currentEntity );
                                queue.add( neighborEntity.getKey() );
                            }
                        } );
            }
        }

        // Node is not reachable
        if (distance.get( destinationEntityKey ) == -1 && prevNodeHop.get( destinationEntityKey ) == null)
            log.info( "No route exist between nodes: source={} and destination={}",
                    sourceEntityKey,
                    destinationEntityKey );
        else
            return Optional.ofNullable( returnTraversedPath( sourceEntityKey, destinationEntityKey, prevNodeHop ) );

        return Optional.empty();
    }

    public void printGraph() {
        log.info( ">>>>>>>>>>>>>>   Printing graph >>>>>>>>>>>>>>>>>>>>>>>" );
        allNodeEntities.forEach( (key, nodeEntity) -> {
            if (nodeEntity.getNeighborEntities() != null) {
                AtomicReference<String> neighborsKeys = new AtomicReference<>( " " );
                nodeEntity.getNeighborEntities().forEach( neighbors -> {
                    neighborsKeys.set( neighborsKeys + neighbors.getKey() + " , " );
                } );

                log.info( "Entity node with key={} has neighborEntities:{}", key, neighborsKeys );

            }
        } );
        log.info( "<<<<<<<<<<<<<< Printing graph end <<<<<<<<<<<<<<<<<<<<<<" );
    }

    private List<NodeEntity> returnTraversedPath(String sourceEntityKey,
                                                 String destinationEntityKey,
                                                 Map<String, String> prevNodeHop) {

        List<String> traversedPath = new ArrayList<>();
        var backwardPathKey = destinationEntityKey;
        while (prevNodeHop.get( backwardPathKey ) != null) {
            traversedPath.add( backwardPathKey );
            backwardPathKey = prevNodeHop.get( backwardPathKey );
        }
        // Source entity obviously does not has previous hop to reach to the source.
        traversedPath.add( sourceEntityKey );
        Collections.reverse( traversedPath );
        traversedPath.forEach( System.out::print );

        return traversedPath.stream()
                .map( allNodeEntities::get )
                .collect( Collectors.toList() );
    }

}
