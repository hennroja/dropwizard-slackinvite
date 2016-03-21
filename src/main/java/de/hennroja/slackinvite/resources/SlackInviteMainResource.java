package de.hennroja.slackinvite.resources;

import de.hennroja.slackinvite.SlackAPI;
import de.hennroja.slackinvite.core.JoinRequest;
import de.hennroja.slackinvite.core.JoinRequestDOA;
import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;
import com.google.common.hash.Hashing;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.*;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by hennroja on 20/03/16.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SlackInviteMainResource {

    private final JoinRequestDOA joinRequestDao;
    private final SlackAPI slackAPI;
    private final SecureRandom random;
    private final static Logger logger = LoggerFactory.getLogger(SlackAPI.class);


    public SlackInviteMainResource(JoinRequestDOA joinRequest, SlackAPI slackAPI, SecureRandom random) {
        this.joinRequestDao = joinRequest;
        this.slackAPI = slackAPI;
        this.random = random;
    }

    @GET
    @Timed
    public Response apiRoot(@QueryParam("name") Optional<String> name) {
        try {
            URI uri = new URI("/");
            return Response.seeOther(uri).build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Response.noContent().build();
    }


    @GET
    @Path("/activate")
    @Timed
    @UnitOfWork
    public Object apiInvite(@QueryParam("email") Optional<String> email,
                            @QueryParam("hash") Optional<String> hash) {

        JoinRequest elem = joinRequestDao.findByMail(email.get());

        if(elem.getHash().equals(hash.get())){
            slackAPI.sendInvite(elem.getEmail(), elem.getFirstname());
            // TODO: Check if req was successful + change 'active' attribute in joinRequest
            return  elem;
        } else {
            return Response.serverError().build();
        }

    }


    @GET
    @Path("/invite")
    @Timed
    public Response apiInviteGet(@QueryParam("email") Optional<String> email) {
        return Response.ok().build();
    }


    @POST
    @Path("/invite")
    @Timed
    @UnitOfWork
    public String apiInvitePost(@FormParam("email") Optional<String> email,
                             @FormParam("firstname") Optional<String> name) {

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setActive(false);
        joinRequest.setFirstname(name.get());
        joinRequest.setHash(sha(random.nextLong()));
        joinRequest.setEmail(email.get());

        joinRequestDao.create(joinRequest);

        logger.debug("localhost:8080/api/activate?email="+joinRequest.getEmail()+"&hash="+joinRequest.getHash());
        //mailService.message("localhost:8080/api/activate?email=\"+joinRequest.getEmail()+\"&hash=\"+joinRequest.getHash()")
        //        .to("webmaster@yourslack.com").send();

        return "Thanks for Requesting !";
    }


    private static String sha(long l) {
        return Hashing.sha256().hashLong(l).toString();
    }
}