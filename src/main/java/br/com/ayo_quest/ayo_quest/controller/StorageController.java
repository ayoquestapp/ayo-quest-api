package br.com.ayo_quest.ayo_quest.controller;


import br.com.ayo_quest.ayo_quest.dto.UploadResponseDTO;
import br.com.ayo_quest.ayo_quest.service.SharePointService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {



    private final SharePointService sharePointService;




    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UploadResponseDTO> upload(
            @RequestParam("file") MultipartFile file
    ){


        String url =
                sharePointService.upload(file);



        return ResponseEntity.ok(
                new UploadResponseDTO(url)
        );

    }


}