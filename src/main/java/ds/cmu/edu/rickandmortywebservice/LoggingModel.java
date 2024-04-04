package ds.cmu.edu.rickandmortywebservice;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LoggingModel {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public LoggingModel(){
        String connectionString = "mongodb://yejunk:jj980503@ac-pxedcus-shard-00-00.8rhmyir.mongodb.net:27017,ac-pxedcus-shard-00-01.8rhmyir.mongodb.net:27017,ac-pxedcus-shard-00-02.8rhmyir.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("RickAndMorty");
        collection = database.getCollection("ramCollection");
    }

    public void logRequestResponse(String option, String searchTerm, String model, String request, Date timestamp, String response){
        Document document = new Document("option", option)
                .append("searchTerm", searchTerm)
                .append("model", model)
                .append("timestamp", timestamp)
                .append("response", response);
        collection.insertOne(document);

        // Read all documents from the database
        System.out.println("Reading all documents from the database:");
        for (Document d : collection.find()) {
            System.out.println(document.getString("userString"));
        }
    }

    public List<Document> getLogs() {
        // Fetch all documents from the collection
        FindIterable<Document> iterDoc = collection.find();

        // Convert the iterable to a list
        List<Document> logs = new ArrayList<>();
        for (Document doc : iterDoc) {
            logs.add(doc);
        }

        return logs;
    }

    public long getOptionCount(String option) {
    // Count the number of documents with the specified option
    return collection.countDocuments(Filters.eq("option", option));
}

    public List<Document> getTopSearchTerms(int number) {
        // Group the documents by search term, count the number of documents in each group, and sort the groups by count
        Bson group = Aggregates.group("$searchTerm", Accumulators.sum("count", 1));
        Bson sort = Aggregates.sort(Sorts.descending("count"));
        Bson limit = Aggregates.limit(number);
        return collection.aggregate(Arrays.asList(group, sort, limit)).into(new ArrayList<>());
    }

    public List<Document> getModelCounts() {
        // Group the documents by model and count the number of documents in each group
        Bson group = Aggregates.group("$model", Accumulators.sum("count", 1));
        return collection.aggregate(Arrays.asList(group)).into(new ArrayList<>());
    }

    public void close() {
        // Close the MongoDB client
        mongoClient.close();
    }
}
