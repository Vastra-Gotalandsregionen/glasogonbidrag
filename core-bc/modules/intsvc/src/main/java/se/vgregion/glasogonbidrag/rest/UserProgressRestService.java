package se.vgregion.glasogonbidrag.rest;

import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Path("/")
@Produces("application/json")
public class UserProgressRestService {

    @Autowired
    private GrantService grantService;

    @GET
    @Path("user-progress/{userId}")
    public Response getData(@PathParam("userId") long userId) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        long progress = grantService.currentProgressByUserAndDate(userId, today);

        Map<String, String> myGoal = new HashMap<>();
        myGoal.put("progress", Long.toString(progress));
        myGoal.put("goal", "6000000");

        return Response.ok().entity(myGoal).build();
    }

    @GET
    @Path("user-progress/{userId}/on/{date}")
    public Response getDataOnDate(@PathParam("userId") long userId,
                                  @PathParam("date") String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            String message = "Parsing date failed, " +
                    "please provide date in the format of 'yyyy-MM-dd'.";

            return Response.status(400).entity(message).build();
        }

        long progress = grantService.currentProgressByUserAndDate(userId, date);

        Map<String, String> myGoal = new HashMap<>();
        myGoal.put("progress", Long.toString(progress));
        myGoal.put("goal", "6000000");

        return Response.ok().entity(myGoal).build();
    }

}
