package kh.com.acleda.student.rest;

import jakarta.validation.Valid;
import kh.com.acleda.student.payload.AsteroidDataReq;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.service.ThirdPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thirdparty")
@RequiredArgsConstructor
public class ThirdPartyAPI {

    private final ThirdPartyService thirdPartyService;

    @PostMapping("/nasa_data")
    public Response<?> getAsteroidDataInRangeDate(@Valid @RequestBody AsteroidDataReq asteroidDataReq){
        return this.thirdPartyService.getAsteroidDataInRangeDate(asteroidDataReq);
    }

    @GetMapping("/nasa_browser")
    public Response<?> getAsteroidDataBrowser(){
        return this.thirdPartyService.getAsteroidDataBrowser();
    }


}
