package org.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/chat")
public class ChatResource {

    @Inject
    EchoAgent agent;

    @GET
    public ChatResponse chat(@QueryParam("q") String q) {
        return agent.chat(q == null ? "hello" : q);
    }
}
