import java.sql.*;
import java.util.Properties;

public class BookingServiceImpl implements BookingService{

    @Override
    public Integer bookSeat(Integer flightId, Integer userId) {
        //1. get first seat from seat table which is not booked
        //2. book seat
        System.out.println(Thread.currentThread());
        Connection connection = null;
        try {
            //connection = createConnection(userId);
            connection = DataSourceConfig.getConnection();
            connection.setAutoCommit(false);
            assert connection != null;
            Statement statement = connection.createStatement();

            Integer seatId = getAvailableSeat(statement);
            if(seatId == null){
                throw new RuntimeException("Unable to get seat");
            }
            bookAvailableSeat(statement, userId, flightId, seatId);
            System.out.println(String.format("Booking confirmed: user_id: %s, seat_id: %s",userId, seatId));
            connection.commit();
            return seatId;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            System.out.println(String.format("Booking failed: user_id: %s",userId));
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private void bookAvailableSeat(Statement statement, Integer userId, Integer flightId, Integer seatId) throws SQLException {

        StringBuilder bookSeatQuery = new StringBuilder("insert into booking (user_id, flight_id, seat_id) values ");
        bookSeatQuery.append(String.format("(%s, %s, %s)",userId,flightId,seatId));
        statement.execute(bookSeatQuery.toString());

        StringBuilder fillInSeatQuery = new StringBuilder("update seat set is_booked = true where id = ");
        fillInSeatQuery.append(String.format("%s;",seatId));
        statement.execute(fillInSeatQuery.toString());
    }

    private Integer getAvailableSeat(Statement statement) throws SQLException {
        //String getSeatQuery = "select id from seat where is_booked = false OFFSET 0 LIMIT 1";
        //String getSeatQuery = "select id from seat where is_booked = false OFFSET 0 LIMIT 1 for share";
//        String getSeatQuery = "select id from seat where is_booked = false OFFSET 0 LIMIT 1 for update";
        String getSeatQuery = "select id from seat where is_booked = false OFFSET 0 LIMIT 1 for update skip locked";
        ResultSet rs = statement.executeQuery(getSeatQuery);
        Integer ans = null;
        while (rs.next()){
            ans = rs.getInt(1);
        }
        return ans;
    }

    private Connection createConnection(Integer userId) throws SQLException {
        String dbURL3 = "jdbc:postgresql://localhost:5432/airline";
        Properties parameters = new Properties();
        parameters.put("user", "chhavis");

        Long timeBeforeConnection = System.currentTimeMillis();
        Connection conn1 = DriverManager.getConnection(dbURL3, parameters);
        if (conn1 != null) {
            System.out.println("Connected to database #1");
            System.out.println(String.format("Time taken to connect to database, user-id: %s, time: %s",userId, System.currentTimeMillis()-timeBeforeConnection));
        }
       return conn1;
    }

}
