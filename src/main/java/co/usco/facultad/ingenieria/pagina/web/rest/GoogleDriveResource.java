package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.service.GoogleDriveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;


@RestController
@RequestMapping("/api")
public class GoogleDriveResource {

    private final Logger log = LoggerFactory.getLogger(GoogleDriveResource.class);

    private final GoogleDriveService googleDriveService;

    public GoogleDriveResource(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @PostMapping("/open/google-drive/resourse")
    public Mono<ResponseEntity<String>> upload(@RequestPart("file")FilePart file) throws Exception {

        return googleDriveService.dataUpload(file).map(res -> {
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityDeletionAlert("as", true, "asdsd", "google"))
                .body(res);
        });

            /* return Mono.from(a -> {
                try {
                    ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityDeletionAlert("as", true, "asdsd", "google"))
                        .body(googleDriveService.dataUpload(file));
                } catch (Exception e) {
                    e.printStackTrace();
                    ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityDeletionAlert("as", true, "asdsd", "google"))
                        .body("aassdsda");
                }
            }); */

        /* return Mono.from(a -> ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert("as", true, "asdsd", "google"))
            .body("aassdsda")); */
    }

    @GetMapping("/open/google-drive")
    public Mono<ResponseEntity<Void>> getGoogleDrive() {
        try {
            log.debug("dentro de controlador");
            googleDriveService.initService();
        } catch (Exception e) {
            log.error("dentro de error");
            e.printStackTrace();
        }
        return Mono.from(a -> ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert("as", true, "asdsd", "google"))
            .build());
    }
}
