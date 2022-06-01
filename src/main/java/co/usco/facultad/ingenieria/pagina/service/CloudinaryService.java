package co.usco.facultad.ingenieria.pagina.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    public Mono<Map<String, Object>> uploadImageVideo(String folder, MultipartFile multipartFile) throws IOException;

    public Mono<Map> uploadImageVideo(String folder, FilePart filePart);

    public Mono<String> uploadImageVideo(FilePart filePart);

    public Mono<String> uploadImageVideo(String base64);

}
