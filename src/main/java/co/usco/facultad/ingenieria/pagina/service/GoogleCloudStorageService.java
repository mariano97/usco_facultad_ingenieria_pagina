package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface GoogleCloudStorageService {

    public Mono<String> uploadFileToStorage(String carpeta, FilePart filePart);

    public Mono<ArchivosProgramaDTO> uploadFileProgramaToStoragee(Long programaId, String carpeta, FilePart filePart);

    public Mono<String> downloadFileFromStorage(String fileName, Long generation);


}
