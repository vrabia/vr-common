package app.vrabia.vrcommon.models.security;

import java.util.ArrayList;
import java.util.List;

public class PublicEndpoints {
    private List<String> endpoints;

    public PublicEndpoints() {
        endpoints = new ArrayList<>();
    }

    public PublicEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }
}
