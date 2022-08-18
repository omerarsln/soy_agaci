import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgreSQL {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        System.out.println("PostgreSQL Menu");
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
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/soy_agaci", "postgres", "Omer66Arslan");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM person WHERE id = " + id );
            int parentid = 0;
            while (rs.next()){
                System.out.println("ID: " + rs.getInt("id") + " " + rs.getString("name") + "'in Soy Agaci");
                parentid = rs.getInt("parent_id");
            }
            ataSorgula(parentid);

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
    private static void ataSorgula(int id){
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/soy_agaci", "postgres", "Omer66Arslan");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM person");
            while ( rs.next() ) {
                if(rs.getInt("id") == id){
                    System.out.println(rs.getInt("id") + " : " + rs.getString("name"));
                    ataSorgula(rs.getInt("parent_id"));
                }
            }
        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    private static void soyundanGelenlerSorgula(int id) {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/soy_agaci", "postgres", "Omer66Arslan");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM person WHERE id = " + id );
            while (rs.next()){
                System.out.println("ID: " + rs.getInt("id") + " " + rs.getString("name") + "'in Soyundan Gelenler");
            }
            soySorgula(id);
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
    private static void soySorgula(int id){
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/soy_agaci", "postgres", "Omer66Arslan");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM person" );

            while ( rs.next() ) {
                if(rs.getInt("parent_id") == id){
                    System.out.println(rs.getInt("id") + " : " + rs.getString("name"));
                    soySorgula(rs.getInt("id"));
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
}
