package edu.upc.eetac.dsa.eventsBCN;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Created by Aitor on 29/11/15.
 */
public class EventsBCNResoucreConfig extends ResourceConfig {
    public EventsBCNResoucreConfig() {
        packages("edu.upc.eetac.dsa.eventsBCN");
        packages("edu.upc.eetac.dsa.eventsBCN.auth");
        register(RolesAllowedDynamicFeature.class);
    }
}
