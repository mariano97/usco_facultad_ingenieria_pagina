package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api")
public class GoogleCloudStorageResource {

    private final Logger log = LoggerFactory.getLogger(GoogleCloudStorageResource.class);

    private final GoogleCloudStorageService googleCloudStorageService;

    public GoogleCloudStorageResource(GoogleCloudStorageService googleCloudStorageService) {
        this.googleCloudStorageService = googleCloudStorageService;
    }

    @PostMapping("/open/google-cloud-storage")
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file")FilePart file,
                                                   @RequestParam("carpeta") String carpeta) {
        log.debug(">>>>>>>>>>>>>>>>Dentro de upload file");
        return googleCloudStorageService.uploadFileToStorage(carpeta, file).map(s -> ResponseEntity.ok().body(s));
    }

    @GetMapping("/open/google-cloud-storage")
    public Mono<ResponseEntity<ByteArrayResource>> downloadFile (@RequestParam("fileName") String fileName,
                                                                 @RequestParam("generation") Long generation) {
        return googleCloudStorageService.downloadFileFromStorage(fileName, generation).map(byteArrayOutputStream -> ResponseEntity
            .ok()
            .body(byteArrayOutputStream));
    }

}
