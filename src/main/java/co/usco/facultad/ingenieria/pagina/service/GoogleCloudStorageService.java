package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface GoogleCloudStorageService {

    public Mono<String> uploadFileToStorage(String carpeta, FilePart filePart);

    public Mono<Map<String, Object>> uploadFileToStorageMap(String carpeta, FilePart filePart);

    public Mono<ArchivosProgramaDTO> uploadFileProgramaToStoragee(Long programaId, Long elementoCatalogoId, String carpeta, FilePart filePart);

    public Mono<ArchivosProgramaDTO> updateFileProgramaToStorage(String carpeta, Long archivosProgramaId, FilePart filePart);

    public Mono<String> downloadFileFromStorage(String fileName, Long generation);

    public Mono<Boolean> deleteFileOfStorage(String fileName, Long generation);


}