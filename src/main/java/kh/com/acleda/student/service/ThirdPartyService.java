package kh.com.acleda.student.service;

import kh.com.acleda.student.dto.AsteroidDataReq;
import kh.com.acleda.student.dto.Response;
import org.springframework.stereotype.Service;

@Service
public interface ThirdPartyService {


    Response<?> getAsteroidDataInRangeDate(AsteroidDataReq asteroidDataReq);

    Response<?> getAsteroidDataBrowser();
}
