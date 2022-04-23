import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Airline {
    public void initialize() {
        Connection conn1 = null;

        try {
            // Connect method #3
            String dbURL3 = "jdbc:postgresql://localhost:5432/airline";
            Properties parameters = new Properties();
            parameters.put("user", "chhavis");

            conn1 = DriverManager.getConnection(dbURL3, parameters);
            if (conn1 != null) {
                System.out.println("Connected to database #1");
            }

            assert conn1 != null;
            Statement statement = conn1.createStatement();

            createTablesInAirlineDb(statement);


            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
//                    Statement statement = conn1.createStatement();
//                    String sqlQueryDrop = "DROP TABLE airline, flight, seat, booking;";
//                    statement.execute(sqlQueryDrop);
//                    statement.close();
                    conn1.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
    private void createTablesInAirlineDb(Statement statement) {
        try {
            String sqlQuery = "CREATE TABLE airline ( id serial PRIMARY KEY, name VARCHAR ( 50 ) UNIQUE NOT NULL);";
            statement.execute(sqlQuery);
            String sqlInsertQuery = "INSERT INTO airline(name) VALUES ('indigo'),('kingfisher'),('airindia');";
            statement.execute(sqlInsertQuery);


            String sqlQuery1 = "CREATE TABLE flight ( id serial PRIMARY KEY, name VARCHAR ( 50 ) UNIQUE NOT NULL, airline_id INT NOT NULL, CONSTRAINT fk_airline_id FOREIGN KEY (airline_id) REFERENCES airline(id));";
            statement.execute(sqlQuery1);
            String sqlInsertQuery1 = "INSERT INTO flight(name, airline_id) VALUES ('indigo1',1),('indigo2',1),('indigo3',1);";
            statement.execute(sqlInsertQuery1);


            String sqlQuery2 = "CREATE TABLE seat ( id serial PRIMARY KEY, flight_id INT NOT NULL, is_booked BOOLEAN NOT NULL DEFAULT false ,CONSTRAINT fk_flight_id FOREIGN KEY (flight_id) REFERENCES flight(id));";
            statement.execute(sqlQuery2);
            StringBuilder sqlInsertQuery2 = new StringBuilder("INSERT INTO seat(flight_id) VALUES ");
            for(int i=1;i<120;i++){
                String num = "(1),";
                sqlInsertQuery2.append(num);
            }
            sqlInsertQuery2.append("(1);");
            statement.execute(sqlInsertQuery2.toString());


            String sqlQuery3 = "CREATE TABLE booking ( id serial PRIMARY KEY, user_id INT NOT NULL ,flight_id INT NOT NULL, CONSTRAINT fk_flight_id FOREIGN KEY (flight_id) REFERENCES flight(id), seat_id INT NOT NULL, CONSTRAINT fk_seat_id FOREIGN KEY (seat_id) REFERENCES seat(id));";
            statement.execute(sqlQuery3);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

}
