package com.sysmech.utility.service.graph.algorithm;

import com.sysmech.utility.service.graph.model.NodeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @BeforeEach
    void setup() {


    }


    @Test
    void givenEmptyGraph_whenKeyAndClassEntity_thenAddEntity() {
        // given
        Graph graph = new Graph();

        // when
        graph.addEntity( "key-1", "classification-1" );

        // then
        assertNotNull( graph.getAllNodeEntities() );
        assertEquals( graph.getAllNodeEntities().size(), 1 );

    }

    @Test
    void givenPopulatedGraph_whenAddNullKeyEntity_thenIgnore() {
        // given
        Graph graph = new Graph();
        graph.addEntity( "key-1", "classification-1" );

        // when
        graph.addEntity( null, null );

        // then
        assertNotNull( graph.getAllNodeEntities() );
        assertEquals( graph.getAllNodeEntities().size(), 1 );

    }


    @Test
    void givenPopulatedGraph_whenExistingKeyEntity_thenIgnore() {
        // given
        Graph graph = new Graph();
        graph.addEntity( "key-1", "classification-1" );

        // when
        graph.addEntity( "key-1", "classification-new" );

        // then
        assertNotNull( graph.getAllNodeEntities() );
        assertEquals( graph.getAllNodeEntities().size(), 1 );

    }



    @Test
    void givenPopulatedGraph_whenEmptyKeyEntity_thenIgnore() {
        // given
        Graph graph = new Graph();
        graph.addEntity( "key-1", "classification-1" );

        // when
        graph.addEntity( "", "" );

        // then
        assertNotNull( graph.getAllNodeEntities() );
        assertEquals( graph.getAllNodeEntities().size(), 1 );

    }

    @Test
    void givenPopulatedGraph_whenAssignAssociation_thenCreateDirectedEdge() {
        // given
        Graph graph = new Graph();
        graph.addEntity( "key-1", "classification-1" );
        graph.addEntity( "key-2", "classification-2" );

        //when
        graph.assignAssociation( "key-1", "key-2" );

        //then
        assertNotNull( graph.getAllNodeEntities().get( "key-1" ).getNeighborEntities() );
        assertEquals( graph.getAllNodeEntities().get( "key-1" ).getNeighborEntities().get( 0 ).getKey(), "key-2" );
        // there is no path from key-2 to key1
        assertNull( graph.getAllNodeEntities().get( "key-2" ).getNeighborEntities() );

    }

    @Test
    void givenPopulatedGraph_whenAssignWrongAssociation_thenIgnore() {
        // given
        Graph graph = new Graph();
        graph.addEntity( "key-1", "classification-1" );
        graph.addEntity( "key-2", "classification-2" );

        //when
        graph.assignAssociation( "key-1", "key-3" );

        //then
        assertNull( graph.getAllNodeEntities().get( "key-1" ).getNeighborEntities() );
        assertNull( graph.getAllNodeEntities().get( "key-3" ) );

    }

    @Test
    void givenPopulatedGraph_whenFindShortestPath_thenReturnPathList() {
        //given
        final Graph exampleGraph = createExampleGraph();

        //when
        final Optional<List<NodeEntity>> shortestPath = exampleGraph.findShortestPath( "1", "7" );

        //then
        assertFalse(shortestPath.isEmpty());
        assertEquals(shortestPath.get().size() ,3);
        assertEquals( shortestPath.get().get( 0 ).getKey(),"1");
        assertEquals( shortestPath.get().get( 1 ).getKey(),"3");
        assertEquals( shortestPath.get().get( 2 ).getKey(),"7");
    }

    @Test
    void givenLoopedGraph_whenFindShortestPath_thenStillReturnPathList() {
        //given
        final Graph exampleGraph = createExampleGraph();

        //when
        final Optional<List<NodeEntity>> shortestPath = exampleGraph.findShortestPath( "2", "3" );

        //then
        assertFalse(shortestPath.isEmpty());
        assertEquals(shortestPath.get().size() ,4);
        assertEquals( shortestPath.get().get( 0 ).getKey(),"2");
        assertEquals( shortestPath.get().get( 1 ).getKey(),"0");
        assertEquals( shortestPath.get().get( 1 ).getKey(),"0");
        assertEquals( shortestPath.get().get( 3 ).getKey(),"3");
    }

    @Test
    void givenGraph_whenFindInReversedDirection_thenReturnEmpty() {
        //given
        final Graph exampleGraph = createExampleGraph();

        //when
        final Optional<List<NodeEntity>> shortestPath = exampleGraph.findShortestPath( "3", "5" );

        //then
        assertTrue(shortestPath.isEmpty());

    }

    @Test
    void givenGraph_whenFindWithInvalidEntity_thenReturnEmpty() {
        //given
        final Graph exampleGraph = createExampleGraph();

        //when
        final Optional<List<NodeEntity>> shortestPath = exampleGraph.findShortestPath( "99999", "0" );

        //then
        assertTrue(shortestPath.isEmpty());

    }


    private Graph createExampleGraph() {
        Graph graph = new Graph();

        graph.addEntity( "0", "link" );
        graph.addEntity( "1", "link" );
        graph.addEntity( "2", "router" );
        graph.addEntity( "3", "bts" );
        graph.addEntity( "4", "bts" );
        graph.addEntity( "5", "gateway" );
        graph.addEntity( "6", "gateway" );
        graph.addEntity( "7", "nat" );

        graph.assignAssociation( "0", "1" );
        graph.assignAssociation( "1", "2" );
        graph.assignAssociation( "2", "0" );
        graph.assignAssociation( "1", "3" );
        graph.assignAssociation( "3", "7" );
        graph.assignAssociation( "5", "3" );
        graph.assignAssociation( "3", "4" );
        graph.assignAssociation( "4", "7" );
        graph.assignAssociation( "4", "6" );
        graph.assignAssociation( "2", "6" );

        return graph;
    }
}