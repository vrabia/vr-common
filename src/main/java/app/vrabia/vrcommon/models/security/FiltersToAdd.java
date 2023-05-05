package app.vrabia.vrcommon.models.security;

import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;
import java.util.List;

public class FiltersToAdd {

    private List<OncePerRequestFilter>  filters;

    public FiltersToAdd() {
        filters = new ArrayList<>();
    }

    public FiltersToAdd(List<OncePerRequestFilter> filters) {
        this.filters = filters;
    }

    public List<OncePerRequestFilter> getFilters() {
        return filters;
    }
}
