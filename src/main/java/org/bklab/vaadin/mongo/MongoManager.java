package org.bklab.vaadin.mongo;

import com.mongodb.*;

public class MongoManager {
    private Mongo mongo;
    private DB greenLight;
    private String host = "10.8.6.5";
    private int port = 27017;
    private String databaseName = "greenlight";


    public MongoManager() {
        try {
            mongo = new Mongo(host, port);
            greenLight = mongo.getDB(databaseName);
//            Set<String> collectionNamesSet = greenLight.getCollectionNames();
//            collectionNamesSet.forEach(collectionName -> System.out.println(collectionName));
            DBCollection reports = greenLight.getCollection("Report");
            DBObject obj = reports.findOne();


            System.out.println("id:" + obj.get("_id"));
            System.out.println("Name:" + obj.get("Name"));
            System.out.println("Author:" + obj.get("Author"));
            System.out.println("Keys:" + obj.get("Keys"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}