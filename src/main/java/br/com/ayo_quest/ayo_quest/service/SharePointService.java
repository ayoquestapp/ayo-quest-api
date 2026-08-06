package br.com.ayo_quest.ayo_quest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SharePointService {


    private final RestTemplate restTemplate =
            new RestTemplate();


    private final ObjectMapper mapper =
            new ObjectMapper();



    @Value("${sharepoint.tenant-id}")
    private String tenantId;


    @Value("${sharepoint.client-id}")
    private String clientId;


    @Value("${sharepoint.client-secret}")
    private String clientSecret;


    @Value("${sharepoint.site-hostname}")
    private String hostname;


    @Value("${sharepoint.site-path}")
    private String sitePath;


    @Value("${sharepoint.folder}")
    private String folder;

    public String upload(MultipartFile file) {

        try {

            String token =
                    getAccessToken();


            String driveId =
                    getDriveId(token);



            String fileName =
                    UUID.randomUUID()
                            + "-"
                            + file.getOriginalFilename();



            String path =
                    folder
                            + "/"
                            + fileName;



            uploadFile(
                    token,
                    driveId,
                    path,
                    file
            );


            return getFileUrl(
                    token,
                    driveId,
                    path
            );


        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro upload SharePoint: "
                            + e.getMessage()
            );
        }

    }

    private String getAccessToken() {


        String url =
                "https://login.microsoftonline.com/"
                        + tenantId
                        + "/oauth2/v2.0/token";



        MultiValueMap<String,String> body =
                new LinkedMultiValueMap<>();


        body.add(
                "grant_type",
                "client_credentials"
        );


        body.add(
                "client_id",
                clientId
        );


        body.add(
                "client_secret",
                clientSecret
        );


        body.add(
                "scope",
                "https://graph.microsoft.com/.default"
        );



        HttpHeaders headers =
                new HttpHeaders();


        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );



        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        url,
                        new HttpEntity<>(body, headers),
                        String.class
                );



        try {

            JsonNode json =
                    mapper.readTree(
                            response.getBody()
                    );


            return json
                    .get("access_token")
                    .asText();


        } catch(Exception e){

            throw new RuntimeException(
                    "Token Microsoft inválido"
            );

        }

    }

    private String getDriveId(String token){


        String url =
                "https://graph.microsoft.com/v1.0/sites/"
                        + hostname
                        + ":"
                        + sitePath
                        + ":/drive?$select=id";



        HttpHeaders headers =
                new HttpHeaders();


        headers.setBearerAuth(token);



        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );


        try {

            JsonNode json =
                    mapper.readTree(
                            response.getBody()
                    );


            return json
                    .get("id")
                    .asText();


        } catch(Exception e){

            throw new RuntimeException(
                    "Não foi possível pegar driveId"
            );

        }

    }

    private void uploadFile(
            String token,
            String driveId,
            String path,
            MultipartFile file
    ) throws Exception {



        String encoded =
                URLEncoder
                        .encode(
                                path,
                                StandardCharsets.UTF_8
                        )
                        .replace("+","%20");



        String url =
                "https://graph.microsoft.com/v1.0/drives/"
                        + driveId
                        + "/root:/"
                        + encoded
                        + ":/content";



        HttpHeaders headers =
                new HttpHeaders();


        headers.setBearerAuth(token);



        headers.setContentType(
                MediaType.parseMediaType(
                        file.getContentType()
                )
        );



        HttpEntity<byte[]> request =
                new HttpEntity<>(
                        file.getBytes(),
                        headers
                );



        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );

    }

    private String getFileId(
            String token,
            String driveId,
            String path
    ){


        String encoded =
                URLEncoder
                        .encode(
                                path,
                                StandardCharsets.UTF_8
                        )
                        .replace("+","%20");



        String url =
                "https://graph.microsoft.com/v1.0/drives/"
                        + driveId
                        + "/root:/"
                        + encoded;



        HttpHeaders headers =
                new HttpHeaders();


        headers.setBearerAuth(token);



        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );



        try {

            JsonNode json =
                    mapper.readTree(
                            response.getBody()
                    );


            return json
                    .get("id")
                    .asText();


        } catch(Exception e){

            throw new RuntimeException(
                    "Erro ao pegar ID arquivo"
            );

        }

    }

    private String getFileUrl(
            String token,
            String driveId,
            String path
    ){


        String encoded =
                URLEncoder
                        .encode(
                                path,
                                StandardCharsets.UTF_8
                        )
                        .replace("+","%20");



        String url =
                "https://graph.microsoft.com/v1.0/drives/"
                        + driveId
                        + "/root:/"
                        + encoded;



        HttpHeaders headers =
                new HttpHeaders();


        headers.setBearerAuth(token);



        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );



        try {


            JsonNode json =
                    mapper.readTree(
                            response.getBody()
                    );



            return json
                    .get("webUrl")
                    .asText();



        }catch(Exception e){


            throw new RuntimeException(
                    "Erro ao buscar URL do arquivo"
            );

        }

    }




}