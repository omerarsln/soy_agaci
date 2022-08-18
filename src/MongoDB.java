import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Scanner;
import java.util.logging.Level;

public class MongoDB {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        Scanner in = new Scanner(System.in);

        System.out.println("MongoDB Menu");
        System.out.println("0 : Çıkış");
        System.out.println("1 : Soy Ağacı Sorgula");
        System.out.println("2 : Soyundan Gelenleri Sorgula");

        System.out.print("Seçenek ? : ");
        int input = in.nextInt();

        while(input != 0){
            int id;
            switch (input) {
                case 1:
                    System.out.print("Soy Ağacı Sorgulanacak ID giriniz : ");
                    id = in.nextInt();
                    soyAgaciSorgula(id);
                    System.out.println("-----------------------------------------");
                    break;
                case 2:
                    System.out.print("Soyundan Gelenler Sorgulanacak ID giriniz : ");
                    id = in.nextInt();
                    soyundanGelenlerSorgula(id);
                    System.out.println("-----------------------------------------");
                    break;
                default:
                    System.out.println("Yanlış Giriş");
            }
            System.out.print("Seçenek ? : ");
            input = in.nextInt();
        }
    }
    private static void soyAgaciSorgula(int id) {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase database = mongo.getDatabase("soy_agaci");
        MongoCollection<Document> collection = database.getCollection("person");

        int parentId = 0;

        FindIterable<Document> results = collection.find(new BasicDBObject("id", id));
        for(Document result: results) {
            System.out.println("ID: " + result.get("id") + " " + result.get("name") + "'in Soy Agaci");
            parentId = (Integer) result.get("parent_id");
            ataSorgula(parentId);
        }
    }
    private static void ataSorgula(int parentId) {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase database = mongo.getDatabase("soy_agaci");
        MongoCollection<Document> collection = database.getCollection("person");

        FindIterable<Document> results = collection.find();
        for(Document result: results) {
            if(result.get("id").equals(parentId)) {
                System.out.println(result.get("id") + " : " +result.get("name"));
                int i = (Integer) result.get("parent_id");
                ataSorgula(i);
            }
        }
    }
    private static void soyundanGelenlerSorgula(int id) {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase database = mongo.getDatabase("soy_agaci");
        MongoCollection<Document> collection = database.getCollection("person");

        FindIterable<Document> results = collection.find(new BasicDBObject("id", id));
        for(Document result: results) {
            System.out.println("ID: " + result.get("id") + " " + result.get("name") + "'in Soyundan Gelenler");
            soySorgula(id);
        }
    }
    private static void soySorgula(int id) {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase database = mongo.getDatabase("soy_agaci");
        MongoCollection<Document> collection = database.getCollection("person");

        FindIterable<Document> results = collection.find();
        for(Document result: results) {
            if(result.get("parent_id").equals(id)) {
                System.out.println(result.get("id") + " : " +result.get("name"));
                int id2 = (Integer) result.get("id");
                soySorgula(id2);
            }
        }
    }
}
