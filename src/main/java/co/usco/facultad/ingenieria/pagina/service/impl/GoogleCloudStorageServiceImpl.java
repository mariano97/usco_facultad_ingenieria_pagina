package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.constants.GoogleServiceProps;
import co.usco.facultad.ingenieria.pagina.service.ArchivosProgramaService;
import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import com.google.cloud.storage.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.channels.Channels;
import java.util.*;

@Service
public class GoogleCloudStorageServiceImpl implements GoogleCloudStorageService {

    private Logger log = LoggerFactory.getLogger(GoogleCloudStorageServiceImpl.class);

    // @Value("google-config.bucket-name")
    private String bucketName = "arched-catwalk-352122.appspot.com";

    /*
    @Value("classpath:statics")
    private String urlFilesStatics;*/

    @Autowired
    private Storage storage;

    private final ArchivosProgramaService archivosProgramaService;

    public GoogleCloudStorageServiceImpl(ArchivosProgramaService archivosProgramaService) {
        this.archivosProgramaService = archivosProgramaService;
    }


    @Override
    public Mono<String> uploadFileToStorage(final String carpeta, FilePart filePart) {
        log.debug(">>>>>>>>Dentro de upload file to Storge: {}, {}", filePart.filename(), filePart.headers().getContentType());
        File file = new File(filePart.filename());
        return filePart.transferTo(file).doOnSuccess(unused -> log.debug(">>>>> conversion satisfactoria"))
            .then(Mono.just(file)).map(newFile -> {
                byte[] bytes = {};
                try {
                    bytes = getArrayBytesFromFile(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newFile.delete();
                return bytes;
            })
            .flatMap(bytes -> {
                String urlMedia = "";
                if (bytes.length > 0) {
                    final BlobId blobId = generateBlobId(bucketName, carpeta, filePart.filename());
                    // BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(filePart.headers().getContentType().getType()).build();
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                    Blob blob = storage.create(blobInfo, bytes);
                    urlMedia = blob.getMediaLink();
                }
                return Mono.just(urlMedia);
            });
    }

    @Override
    public Mono<ArchivosProgramaDTO> uploadFileProgramaToStoragee(Long programaId, String carpeta, FilePart filePart) {
        File file = new File(filePart.filename());
        return filePart.transferTo(file).doOnSuccess(unused -> log.debug(">>>>> conversion satisfactoria"))
            .then(Mono.just(file)).map(newFile -> {
                byte[] bytes = {};
                try {
                    bytes = getArrayBytesFromFile(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newFile.delete();
                return bytes;
            })
            .map(bytes -> {
                Map mapsProps = new HashMap();
                if (bytes.length > 0) {
                    final BlobId blobId = generateBlobId(bucketName, carpeta, filePart.filename());
                    // BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(filePart.headers().getContentType().getType()).build();
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                    Blob blob = storage.create(blobInfo, bytes);
                    mapsProps.put(GoogleServiceProps.PROP_NAME_FILE_UPLOAD, blob.getName());
                    mapsProps.put(GoogleServiceProps.PROP_MEDIA_LINK_FILE_UPLOAD, blob.getMediaLink());
                    mapsProps.put(GoogleServiceProps.PROP_GENERATION_FILE_UPLOAD, blob.getGeneration());

                }
                return mapsProps;
            })
            .flatMap(map -> {
                ArchivosProgramaDTO archivosProgramaDTO = new ArchivosProgramaDTO();
                ProgramaDTO programaDTO = new ProgramaDTO();
                programaDTO.setId(programaId);
                archivosProgramaDTO.setPrograma(programaDTO);
                archivosProgramaDTO.setGenerationStorage((Long) map.get(GoogleServiceProps.PROP_GENERATION_FILE_UPLOAD));
                archivosProgramaDTO.setUrlName((String) map.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                archivosProgramaDTO.setPlanEstudio(false);
                return archivosProgramaService.save(archivosProgramaDTO);
            });
    }

    @Override
    public Mono<String> downloadFileFromStorage(String fileName, Long generation) {
        return Mono.just(storage).map(googleStorage -> {
            Blob blob = googleStorage.get(BlobId.of(bucketName, fileName, generation));
            return blob.reader();
        }).flatMap(readChannel -> {
            InputStream inputStream = Channels.newInputStream(readChannel);
            byte[] content = {};
            // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(0);
            ByteArrayResource byteArrayResource = new ByteArrayResource(content);
            try {
                content = IOUtils.toByteArray(inputStream);
                /* byteArrayOutputStream = new ByteArrayOutputStream(content.length);
                byteArrayOutputStream.write(content, 0, content.length); */
                byteArrayResource = new ByteArrayResource(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayResource = new ByteArrayResource(content);
            return Mono.just(Base64.getEncoder().encodeToString(content));
        });
    }

    private byte[] getArrayBytesFromFile(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int)file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    private BlobId generateBlobId(String bucketName, @Nullable String directory, String filename) {
        log.debug(">>>>>>>>>< bucketNmae: {}", bucketName);
        String filenameNew = UUID.randomUUID().toString().concat("_")
            .concat(filename.replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9\\s.-]", "_"));

        log.debug(">>>>>>> filename: {}", filename);
        return Optional.ofNullable(directory)
            .map(s -> BlobId.of(bucketName, directory + "/" + filenameNew))
            .orElse(BlobId.of(bucketName, filenameNew));
    }

}
