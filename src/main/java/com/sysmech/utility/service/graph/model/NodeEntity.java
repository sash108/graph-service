package com.sysmech.utility.service.graph.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class NodeEntity {

    String key;
    String classType;
    List<NodeEntity> neighborEntities;

    public void addNeighbor(NodeEntity neighborNodeEntity) {
        if (neighborEntities == null)
            neighborEntities = new ArrayList<>();
        neighborEntities.add( neighborNodeEntity );
    }

}

