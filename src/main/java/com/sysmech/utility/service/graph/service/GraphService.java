package com.sysmech.utility.service.graph.service;

import com.sysmech.utility.service.graph.algorithm.Graph;
import generated.Topology;
import org.springframework.stereotype.Service;

public interface GraphService {
     Graph createGraph(final Topology topology);
}
