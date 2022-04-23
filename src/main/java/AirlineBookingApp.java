import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class AirlineBookingApp {

    public static void main(String[] args) throws InterruptedException {

        Airline airline = new Airline();
        airline.initialize();

        BookingService bookingService = new BookingServiceImpl();

        Long timeBeforeExec = System.currentTimeMillis();

//        for(int i=0;i<120;i++){
//            bookingService.bookSeat(1,i+1);
////            System.out.println(String.format("Booking confirmed for user: %s",i+1));
//        }

        ExecutorService threadPool = Executors.newFixedThreadPool(8);

//        ExecutorService threadPool = Executors.newScheduledThreadPool(8);
//        List<CompletableFuture<Integer>> bookSeatCf = new ArrayList<>();
        List<Callable<Integer>> futureList = new ArrayList<Callable<Integer>>();

        for(int i=0;i<120;i++){
            int finalI = i;
//            bookSeatCf.add(CompletableFuture.supplyAsync(() -> bookingService.bookSeat(1, finalI +1),threadPool));
            futureList.add(() -> bookingService.bookSeat(1,finalI+1));
        }

//        CompletableFuture.allOf(bookSeatCf.toArray(new CompletableFuture[bookSeatCf.size()]))
//                        .join();

        threadPool.invokeAll(futureList);

        System.out.println(String.format("Time taken to book seats: %s", System.currentTimeMillis()-timeBeforeExec));

    }

}

/*
* AIRLINE - ID, NAME
* FLIGHT - ID, NAME, AIRLINE_ID, SOURCE, DESTINATION, TAKEOFF_TIME, LANDING_TIME
* USERS - ID, NAME, ADDRESS, AGE
* BOOKING - ID, USER_ID, FLIGHT_ID, SEAT_ID
* SEAT - FLIGHT_ID, SEAT_ID, IS_BOOKED
* */
