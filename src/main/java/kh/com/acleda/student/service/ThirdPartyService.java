package kh.com.acleda.student.service;

import kh.com.acleda.student.payload.AsteroidDataReq;
import kh.com.acleda.student.payload.Response;
import org.springframework.stereotype.Service;

@Service
public interface ThirdPartyService {


    Response<?> getAsteroidDataInRangeDate(AsteroidDataReq asteroidDataReq);

    Response<?> getAsteroidDataBrowser();


}
