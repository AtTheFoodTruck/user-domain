package com.sesac.foodtruckuser.application.service.api;

import com.sesac.foodtruckuser.ui.dto.api.ApiReqStatusDto;
import com.sesac.foodtruckuser.ui.dto.api.ApiReqValidateDto;
import com.sesac.foodtruckuser.ui.dto.request.BNoApiRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Configuration
public class BNoApiRestTemplate {

    private final RestTemplate restTemplate;

    public BNoApiRestTemplate() {
        this.restTemplate = new RestTemplate();
    }

    public boolean statusApi(BNoApiRequestDto.BNoStatusDto statusDto) {
        // UriComponents
        URI uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/status")
                .queryParam("serviceKey", "JyZTPPmD5XHt0PIhYecvp1xIsQj%2B1kU%2Btw4P%2Be2UHoqKCIdQ2gM5aQvJCGDrWh4LRE9fv7YOZIlNuj2o0asNDA%3D%3D")
                .build(true)
                .encode()
                .toUri();

        ApiReqStatusDto dto = ApiReqStatusDto.builder()
                                            .bNo(statusDto.getBNo())
                                            .build();

        HttpEntity<ApiReqStatusDto> entity = new HttpEntity<>(dto, new HttpHeaders());

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uriComponents, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject body;
        try {
            body = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException e) {
            log.warn("Json Parsing Error: 사업자등록번호 상태조회 실패");
            return false;
        }

        return "OK".equals(body.get("status_code")) && body.containsKey("match_cnt");
    }

    public boolean validateApi(BNoApiRequestDto.BNoValidateDto validateDto) {
        URI uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/validate")
                .queryParam("serviceKey", "JyZTPPmD5XHt0PIhYecvp1xIsQj%2B1kU%2Btw4P%2Be2UHoqKCIdQ2gM5aQvJCGDrWh4LRE9fv7YOZIlNuj2o0asNDA%3D%3D")
                .build(true)
                .encode()
                .toUri();

        ApiReqValidateDto dto = ApiReqValidateDto.builder()
                                            .bNoValidateDto(validateDto)
                                            .build();

        HttpEntity<ApiReqValidateDto> entity = new HttpEntity<>(dto, new HttpHeaders());

        ResponseEntity<String> response = null;
        try {
           response = restTemplate.exchange(uriComponents, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject body;
        try {
            body = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException e) {
            log.warn("Json Parsing Error: 사업자등록번호 진위확인 실패");
            return false;
        }

        return "OK".equals(body.get("status_code")) && body.containsKey("valid_cnt");
    }
}
