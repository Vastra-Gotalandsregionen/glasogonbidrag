package se.vgregion.glasogonbidrag.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martlin on 2016/05/11.
 */
@Path("/")
@Produces("application/json")
public class MyGoalRestService {
    @GET
    @Path("my-goal")
    public Response getData() {
        Map<String, String> myGoal = new HashMap<>();
        myGoal.put("progress", "2700000");
        myGoal.put("goal", "6000000");

        return Response.ok().entity(myGoal).build();
    }
}
