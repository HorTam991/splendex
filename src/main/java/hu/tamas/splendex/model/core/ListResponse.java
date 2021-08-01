package hu.tamas.splendex.model.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResponse {

    private Long total;

    private List rows;

    public ListResponse(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

}
