package uk.gov.hmcts.reform.idam.service.remote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaleUsersResponse {
    @JsonProperty("staleUsers")
    private List<Long> staleUsersResponse;

    @JsonProperty("moreRecords")
    private Boolean moreRecords;

}
