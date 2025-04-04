
import Database.DatabaseConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            System.out.println("Connected to MSSQL successfully!");
        } else {
            System.out.println("Failed to connect to MSSQL.");
        }
    }
}
