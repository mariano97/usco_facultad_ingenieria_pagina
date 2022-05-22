package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.service.GoogleDriveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
