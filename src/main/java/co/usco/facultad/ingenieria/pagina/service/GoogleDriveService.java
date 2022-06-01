package co.usco.facultad.ingenieria.pagina.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleDriveService {

    void initService() throws Exception;

    public Mono<String> dataUpload(FilePart file) throws Exception;

}
