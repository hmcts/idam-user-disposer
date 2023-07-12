package uk.gov.hmcts.reform.idam.service.remote.responses;

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
    private List<String> staleUsersResponse;

    @JsonProperty("moreRecords")
    private Boolean moreRecords;

}
