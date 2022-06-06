package co.usco.facultad.ingenieria.pagina.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

public interface GoogleCloudStorageService {

    public Mono<String> uploadFileToStorage(String carpeta, FilePart filePart);

    public Mono<String> downloadFileFromStorage(String fileName, Long generation);


}
