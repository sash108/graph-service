package com.sysmech.utility.service.graph.service.impl;

import com.sysmech.utility.service.graph.algorithm.Graph;
import com.sysmech.utility.service.graph.service.GraphService;
import generated.Topology;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl implements GraphService {


    @Override
    public Graph createGraph(Topology topology) {
        final var graph = new Graph();

        // Initialize the graph and add entities to the graph
        if (topology != null && topology.getEntities() != null && topology.getEntities().getClazz() != null) {

            topology.getEntities()
                    .getClazz()
                    .forEach( classification -> {

                        if (classification.getEntity() != null)
                            classification.getEntity()
                                    .forEach( entity -> graph.addEntity( entity.getKey(), classification.getKey() ) );

                    } );
        }

        //assign all associations
        if (topology != null && topology.getAssociations() != null && topology.getAssociations().getAssociation() != null) {

            topology.getAssociations()
                    .getAssociation()
                    .forEach( association -> graph.assignAssociation( association.getPrimary(), association.getSecondary() ) );

        }

        return graph;
    }
}
