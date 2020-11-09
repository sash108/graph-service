# Graph Shortest path utility 

# How to run
     // Download solution and save it to your drive.
     
     git clone https://github.com/logical-dev/graph-service.git
     mvn clean install
     java -jar target/service-graph-0.0.1-SNAPSHOT.jar 
     
     // if you want to give your own topology but schema must be the same.
     java -DFilePath="[Your directory where file is placed]/example.topology" -jar target/service-graph-0.0.1-SNAPSHOT.jar 
     
     // Insert source and destination entities to find shortest path. The program will run indefinitely.
     
     // To finish the program. 
     Hit Control-Exit to exit the program. 

# Solution Approach
- I used a classical BSF algorithm to solve the problem. 
- I created a simple project using spring boot. In the future, it would be easy to transform projects into micro service. 
- For deserializing xml topology, I extracted Topology class model from the given xml schema file and used maven plugin org.codehaus.mojo:jaxb2-maven-plugin.
    - If we changed our schema then it would not be a big change.
- I used lombok library for readable code and to reduce boilerplate code.
- Even though, I used Scanner class to take user input, but I still show results with the help of SLF4J log instead of console logs.
    - There are high chances that the service will grow and change into a RESTFUL API. Then we do not need to do much changes. 
 
# Future Enhancement
Some suggestions for future 
- Enhance functionality to include new topology 
    - Either merge with the existing graph 
    - or have option to replace the existing graph
- Changes in the existing graph
    - Add nodes CRUD operations functionalities in given graph
    - Same true for association or un-association of entities in the graph. 
        - then RESTFUL API is the best candidate for it. 
- Persist the graph nodes (entities) and association information in database.
    - make application kind of stateful, 
    - application resume after each restart where it left. 
- Convert project into RestFul API project in order to give service to other services as well. 
- Perhaps better presentation of taking input and presenting output if we do not implement RESTFUL API project
- Might be excellent to ship through docker images. 
 