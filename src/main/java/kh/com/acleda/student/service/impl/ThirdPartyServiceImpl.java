package kh.com.acleda.student.service.impl;

import kh.com.acleda.student.dto.AsteroidDataReq;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.service.ThirdPartyService;
import kh.com.acleda.student.utils.InterfaceAdapter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private static final Logger log = LogManager.getLogger(ThirdPartyServiceImpl.class);

    private final InterfaceAdapter interfaceAdapter;

    @Override
    public Response<?> getAsteroidDataInRangeDate(AsteroidDataReq asteroidDataReq) {

        String baseUrl = "https://api.nasa.gov/neo/rest/v1/feed";

        String finalUrl = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("start_date", asteroidDataReq.getStartDate())
                .queryParam("end_date", asteroidDataReq.getEndDate())
                .queryParam("api_key", "DEMO_KEY")
                .toUriString();

        return this.interfaceAdapter.callExternalService(finalUrl, "GET", new JSONObject(), "");
    }

    @Override
    public Response<?> getAsteroidDataBrowser() {
        String baseUrl = "https://api.nasa.gov/neo/rest/v1/feed";

        String finalUrl = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("api_key", "DEMO_KEY")
                .toUriString();

        return this.interfaceAdapter.callExternalServiceRetry(finalUrl, "GET", new JSONObject(), "");
    }
}
