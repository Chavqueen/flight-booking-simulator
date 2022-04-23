import java.sql.SQLException;

public interface BookingService {
    public Integer bookSeat(Integer flightId, Integer userId);
}
