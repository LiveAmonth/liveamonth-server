package teamproject.lam_server.global.dto;

import lombok.Data;
import teamproject.lam_server.constants.CategoryConstants.OrderByStatus;

import javax.validation.constraints.Min;

@Data
public class OtherContentSearch {

    private OrderByStatus orderBy;

    @Min(0)
    private int limit;

}