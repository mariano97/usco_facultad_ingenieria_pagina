package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface GoogleCloudStorageService {

    public Mono<String> uploadFileToStorage(String carpeta, FilePart filePart);

    public Mono<Map<String, Object>> uploadFileToStorageMap(String carpeta, FilePart filePart);

    public Mono<ArchivosProgramaDTO> uploadFileProgramaToStoragee(String contentType, Long programaId, Long elementoCatalogoId, String carpeta, FilePart filePart);

    public Mono<ArchivosProgramaDTO> updateFileProgramaToStorage(String contentType, String carpeta, Long archivosProgramaId, FilePart filePart);

    public Mono<ProfesorDTO> uploadFotoProfesorToStorage(String contentType, Long profesorId, String carpeta, FilePart filePart);

    public Mono<String> downloadFileFromStorage(String fileName, Long generation);

    public Mono<String> downloadFileFromStorage(String fileName);

    public Mono<Boolean> deleteFileOfStorage(String fileName);

    public Mono<Boolean> deleteFileOfStorage(String fileName, Long generation);

    public Mono<Void> deleteArchivProgramaUploaded(Long archivoProgramaId);
}
