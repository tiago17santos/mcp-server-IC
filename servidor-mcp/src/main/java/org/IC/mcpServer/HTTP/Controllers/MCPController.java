package org.IC.mcpServer.HTTP.Controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.IC.mcpServer.Core.JsonRpcHandler;
import org.jboss.logging.Logger;

@Path("/mcp")
@ApplicationScoped
public class MCPController {

    private static final Logger LOG = Logger.getLogger(MCPController.class);

    @Inject
    JsonRpcHandler handler;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String handleRequest(String requestJson) {
        LOG.info("[HTTP] Request: " + requestJson);
        String response = handler.handle(requestJson);
        LOG.info("[HTTP] Response: " + response);
        return response;
    }
}
