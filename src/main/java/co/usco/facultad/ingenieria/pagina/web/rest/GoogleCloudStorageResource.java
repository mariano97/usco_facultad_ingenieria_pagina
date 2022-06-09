package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class GoogleCloudStorageResource {

    private final Logger log = LoggerFactory.getLogger(GoogleCloudStorageResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "googleCloudStorage";

    private final GoogleCloudStorageService googleCloudStorageService;

    public GoogleCloudStorageResource(GoogleCloudStorageService googleCloudStorageService) {
        this.googleCloudStorageService = googleCloudStorageService;
    }

    @PostMapping(value = {
        "/open/google-cloud-storage",
        "/google-cloud-storage/upload"
    })
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file")FilePart file,
                                                   @RequestParam("carpeta") String carpeta) {
        log.debug(">>>>>>>>>>>>>>>>Dentro de upload file");
        return googleCloudStorageService.uploadFileToStorage(carpeta, file).map(s -> ResponseEntity.ok().body(s));
    }

    @PostMapping(value = {
        "/google-cloud-storage/programa-upload-files"
    })
    public Mono<ResponseEntity<ArchivosProgramaDTO>> programaUploadFile(@RequestParam("carpeta") String carpeta,
                                                                        @RequestParam("contentType") String contentType,
                                                                        @RequestParam("programaId") Long programaId,
                                                                        @RequestParam("elementoCatalogoId") Long elementoCatalogoId,
                                                                        @RequestPart("file") FilePart file) {
        if (programaId == null || programaId == 0L) {
            throw new BadRequestAlertException("Es necesario tener id del programa", ENTITY_NAME, "idNecesario");
        }
        if (elementoCatalogoId == null || elementoCatalogoId == 0L) {
            elementoCatalogoId = 6L;
        }
        return googleCloudStorageService.uploadFileProgramaToStoragee(contentType, programaId, elementoCatalogoId, carpeta, file)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/google-cloud-storage/programa-upload-files/" + programaId.toString()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
    }

    @PutMapping(value = {
        "google-cloud-storage/programa-upload-files"
    })
    public Mono<ResponseEntity<ArchivosProgramaDTO>> updateProgramaUploadFile(@RequestParam("carpeta") String carpeta,
                                                                              @RequestParam("contentType") String contentType,
                                                                              @RequestParam("archivosProgramaId") Long archivosProgramaId,
                                                                              @RequestPart("file") FilePart filePart) {
        if (archivosProgramaId == null || archivosProgramaId == 0L) {
            throw new BadRequestAlertException("Es necesario tener id del archivos programa", ENTITY_NAME, "idNecesario");
        }
        return googleCloudStorageService.updateFileProgramaToStorage(contentType, carpeta, archivosProgramaId, filePart)
            .map(result -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result));
    }

    @GetMapping(value = {
        "/open/google-cloud-storage/download",
        "/google-cloud-storage/download"
    })
    public Mono<ResponseEntity<String>> downloadFile (@RequestParam("fileName") String fileName,
                                                                 @RequestParam("generation") Long generation) {
        return googleCloudStorageService.downloadFileFromStorage(fileName, generation).map(byteArrayOutputStream -> ResponseEntity
            .ok()
            .body(byteArrayOutputStream));
    }

    @DeleteMapping(value = {
        "/google-cloud-storage"
    })
    public Mono<ResponseEntity<Void>> deleteArchivoProgramaUploaded(@RequestParam("archivoProgramaId") Long archivoProgramaId) {
        if (archivoProgramaId == null || archivoProgramaId == 0L) {
            throw new BadRequestAlertException("Es necesario tener id del archivos programa", ENTITY_NAME, "idNecesario");
        }
        return googleCloudStorageService.deleteArchivProgramaUploaded(archivoProgramaId).map(result -> ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, archivoProgramaId.toString()))
            .build());
    }

}
