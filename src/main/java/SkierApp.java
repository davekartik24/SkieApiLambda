import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import dao.ResortsLiftRidesDao;
import model.LiftRide;
import model.LiftRideDetailsRequest;
import model.ResortsLiftRides;
import model.ResponseMsg;

import java.sql.SQLException;

public class SkierApp implements RequestHandler<LiftRideDetailsRequest, String> {

    private static final Gson gson = new Gson();

    public String handleRequest(LiftRideDetailsRequest liftRideDetailsRequest, Context context) {

        if (liftRideDetailsRequest.getHttpMethod().equals("GET")) {
            try {
                ResortsLiftRidesDao resortsLiftRidesDao = new ResortsLiftRidesDao();
                int totalVertical = resortsLiftRidesDao
                        .getTotalVertical(Integer.parseInt(liftRideDetailsRequest.getResortID()),
                                liftRideDetailsRequest.getSeasonID(),
                                liftRideDetailsRequest.getDayID(),
                                Integer.parseInt(liftRideDetailsRequest.getSkierID()));
                return gson.toJson(totalVertical);
            } catch (ClassNotFoundException cex) {
                ResponseMsg output = new ResponseMsg().message("Internal Server Error" + cex.getMessage());
                return gson.toJson(output);
            } catch (SQLException se) {
                ResponseMsg output = new ResponseMsg().message("Data Not Found: " + se.getMessage());
                return gson.toJson(output);
            }
        }

        try {
            LiftRide reqLifeRide = liftRideDetailsRequest.getLiftRide();
            ResortsLiftRidesDao resortsLiftRidesDao = new ResortsLiftRidesDao();
            int resortId = Integer.parseInt(liftRideDetailsRequest.getResortID());
            String seasonId = liftRideDetailsRequest.getSeasonID();
            String dayId = liftRideDetailsRequest.getDayID();
            int skierId = Integer.parseInt(liftRideDetailsRequest.getSkierID());
            int liftId = reqLifeRide.getLiftID();
            int rideTime = reqLifeRide.getTime();

            StringBuilder sb = new StringBuilder();
            sb.append(resortId).append(seasonId).append(dayId).append(skierId).append(rideTime);

            resortsLiftRidesDao
                    .createLiftRide(new ResortsLiftRides(sb.toString(), resortId, seasonId, dayId, skierId, liftId, rideTime, (liftId * 10)));
            ResponseMsg output = new ResponseMsg().message("Write successful");
            return gson.toJson(output);
        } catch (ClassNotFoundException cex) {
            ResponseMsg output = new ResponseMsg().message("Internal Server Error" + cex.getMessage());
            return gson.toJson(output);
        } catch (SQLException se) {
            ResponseMsg output = new ResponseMsg().message("Data Not Found: " + se.getMessage());
            return gson.toJson(output);
        } catch (Exception ex) {
            ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
            return gson.toJson(output);
        }
    }

    public static void main(String[] args) {

    }
}
