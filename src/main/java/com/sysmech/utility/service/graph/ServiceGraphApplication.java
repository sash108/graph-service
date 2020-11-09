package com.sysmech.utility.service.graph;

import com.sysmech.utility.service.graph.algorithm.Graph;
import com.sysmech.utility.service.graph.model.NodeEntity;
import com.sysmech.utility.service.graph.service.GraphService;
import generated.Topology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class ServiceGraphApplication implements ApplicationRunner {

    static String GRAPH_FILE_NAME = "example.topology";

    @Autowired
    private GraphService graphService;

    @Autowired
    private ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run( ServiceGraphApplication.class, args );
    }

    @Override
    public void run(ApplicationArguments args) {

        final Topology topology = ParseFile();
        final Graph graph = graphService.createGraph( topology );
        graph.printGraph();

        final var scanner = new Scanner( System.in );
        while (true) {
            log.info( "************************ Searching Path ***********************************" );
            log.info( "Welcome to graph searching functionalities. " );
            log.info( "Please enter source and destination key in order to find shortest path." );
            log.info( "Enter Source Entity Key:" );
            String source = scanner.nextLine();
            log.info( "Enter Destination Entity Key:" );
            String destination = scanner.nextLine();
            final Optional<List<NodeEntity>> searchedPath = graph.findShortestPath( source, destination );
            printPathOrder( searchedPath, source, destination );

        }
    }

    private Topology ParseFile() {

        InputStream inputStream;
        JAXBContext jaxbContext;
        Topology topology = null;

        try {
            final var fileName = System.getProperty( "FilePath" );
            if (fileName != null) {
                log.info( "External file is used. " );
                var file = new File( fileName );
                inputStream = new FileInputStream( file );
            } else {
                log.info( "Internal file is used. " );
                Resource resource = resourceLoader.getResource( "classpath:" + GRAPH_FILE_NAME );
                inputStream = resource.getInputStream();
            }

            jaxbContext = JAXBContext.newInstance( Topology.class );
            var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            topology = (Topology) jaxbUnmarshaller.unmarshal( inputStream );

        } catch (IOException e) {
            log.error( "On startup, error while loading data file¬¬.....  ", e );
            System.exit( 0 );
        } catch (JAXBException e) {
            log.error( "On startup, error while parsing data file.....  ", e );
            System.exit( 0 );
        }
        return topology;
    }

    private void printPathOrder(Optional<List<NodeEntity>> searchedPath, String sourceEntityKey, String destinationEntityKey) {

        if (searchedPath.isPresent()) {
            log.info( "Following route exists between nodes: source={} and destination={}", sourceEntityKey, destinationEntityKey );
            searchedPath.ifPresent( pathList -> pathList.forEach(
                    path -> log.info( "[ key:{} , classification:{} ] ", path.getKey(), path.getClassType() )
            ) );
        } else
            log.info( "No route exists between nodes: source={} and destination={}", sourceEntityKey, destinationEntityKey );

    }
}
