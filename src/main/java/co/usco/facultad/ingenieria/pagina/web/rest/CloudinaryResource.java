package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CloudinaryResource {

    private Logger log = LoggerFactory.getLogger(CloudinaryResource.class);

    private static final String ENTITY_NAME = "CloudinaryResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CloudinaryService cloudinaryService;

    public CloudinaryResource(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/open/cloud-upload"
    )
    public ResponseEntity<String> upload(@RequestBody String imageBase64) {
        log.debug("dentro de upload method");
        cloudinaryService.uploadImageVideo(imageBase64);
        return ResponseEntity.ok().body("a");
    }


    @PostMapping(value = {"/open/cloudinary"}
    )
    public Mono<ResponseEntity<String>> getCloudinary(
                                                    @RequestPart("dataFile") FilePart file) throws IOException {
        log.debug("dentro de rest cloudinary");

        //log.debug("name: {}", file.filename());


        // file.doOnSuccess(a -> log.debug("name: {}", a.filename()));
        // file.doFinally(a -> log.debug("finally: {}", a.toString()));
         /*file.flatMap(f -> {
            log.debug("{}", f.filename());
            return null;
        }); */
        return cloudinaryService.uploadImageVideo( file)
            .map(res -> {
                if (res != null) {
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, res))
                        // .body((String) res.get("url"));
                        .body(res);
                } else {
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createAlert(applicationName, ENTITY_NAME, "error"))
                        .body("Error");
                }
            });
        // return Mono.just(ResponseEntity.ok().body("hecho"));
        /* return cloudinaryService.uploadImageVideo("prueba", file)
            .map(result ->
                ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createAlert(applicationName, "File upload", file.getOriginalFilename()))
                    .body((String) result.get("secure_url"))
            ); */
    }

}
