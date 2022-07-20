package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.constants.GoogleServiceProps;
import co.usco.facultad.ingenieria.pagina.service.*;
import co.usco.facultad.ingenieria.pagina.service.dto.*;
import com.google.cloud.ReadChannel;
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

    private final ProfesorService profesorService;

    private final NoticiaService noticiaService;

    private final EventoService eventoService;

    private final SemilleroService semilleroService;

    public GoogleCloudStorageServiceImpl(ArchivosProgramaService archivosProgramaService, ProfesorService profesorService, NoticiaService noticiaService, EventoService eventoService, SemilleroService semilleroService) {
        this.archivosProgramaService = archivosProgramaService;
        this.profesorService = profesorService;
        this.noticiaService = noticiaService;
        this.eventoService = eventoService;
        this.semilleroService = semilleroService;
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
    public Mono<Map<String, Object>> uploadFileToStorageMap(String carpeta, FilePart filePart) {
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
                Map<String, Object> mapsProps = new HashMap<>();
                if (bytes.length > 0) {
                    final BlobId blobId = generateBlobId(bucketName, carpeta, filePart.filename());
                    // BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(filePart.headers().getContentType().getType()).build();
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                    Blob blob = storage.create(blobInfo, bytes);
                    mapsProps.put(GoogleServiceProps.PROP_NAME_FILE_UPLOAD, blob.getName());
                    mapsProps.put(GoogleServiceProps.PROP_MEDIA_LINK_FILE_UPLOAD, blob.getMediaLink());
                    mapsProps.put(GoogleServiceProps.PROP_GENERATION_FILE_UPLOAD, blob.getGeneration());

                }
                return Mono.just(mapsProps);
            });
    }

    @Override
    public Mono<ArchivosProgramaDTO> uploadFileProgramaToStoragee(String contentType, Long programaId, Long elementoCatalogoId, String carpeta, FilePart filePart) {

        return uploadFileToStorageMap(carpeta, filePart)
            .flatMap(map -> {
                ArchivosProgramaDTO archivosProgramaDTO = new ArchivosProgramaDTO();
                ProgramaDTO programaDTO = new ProgramaDTO();
                TablaElementoCatalogoDTO tablaElementoCatalogoDTO = new TablaElementoCatalogoDTO();
                tablaElementoCatalogoDTO.setId(elementoCatalogoId);
                programaDTO.setId(programaId);
                archivosProgramaDTO.setNombreArchivo(filePart.filename());
                archivosProgramaDTO.setPrograma(programaDTO);
                archivosProgramaDTO.setTipoDocumento(contentType);
                archivosProgramaDTO.setGenerationStorage((Long) map.get(GoogleServiceProps.PROP_GENERATION_FILE_UPLOAD));
                archivosProgramaDTO.setUrlName((String) map.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                archivosProgramaDTO.setTablaElementoCatalogo(tablaElementoCatalogoDTO);
                archivosProgramaDTO.setPlanEstudio(elementoCatalogoId == 7);
                return archivosProgramaService.save(archivosProgramaDTO);
            });
    }

    @Override
    public Mono<ArchivosProgramaDTO> updateFileProgramaToStorage(String contentType, String carpeta, Long archivosProgramaId, FilePart filePart) {
        return uploadFileToStorageMap(carpeta, filePart).flatMap(stringObjectMap -> {
            return archivosProgramaService.findOne(archivosProgramaId).map(archivosProgramaDTO -> {
                    deleteFileOfStorage(archivosProgramaDTO.getUrlName(), archivosProgramaDTO.getGenerationStorage())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>File Object delete")).subscribe(aBoolean -> log.debug("file eliminado"));
                    archivosProgramaDTO.setTipoDocumento(contentType);
                    archivosProgramaDTO.setNombreArchivo(filePart.filename());
                    archivosProgramaDTO.setGenerationStorage((Long) stringObjectMap.get(GoogleServiceProps.PROP_GENERATION_FILE_UPLOAD));
                    archivosProgramaDTO.setUrlName((String) stringObjectMap.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                    return archivosProgramaDTO;
                })
                .flatMap(archivosProgramaService::update);
        });
    }

    @Override
    public Mono<ProfesorDTO> uploadFotoProfesorToStorage(String contentType, Long profesorId, String carpeta, FilePart filePart) {
        return uploadFileToStorageMap(carpeta, filePart).flatMap(stringObjectMap ->
                profesorService.findOne(profesorId).flatMap(profesorDTO -> {
                    if (profesorDTO.getUrlFotoProfesor() != null && !profesorDTO.getUrlFotoProfesor().isBlank()) {
                        deleteFileOfStorage(profesorDTO.getUrlFotoProfesor())
                            .doOnSuccess(aBoolean -> log.debug(">>>>>File Object delete")).subscribe(aBoolean -> log.debug("file eliminado"));
                    }
                    profesorDTO.setUrlFotoProfesor((String) stringObjectMap.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                    return profesorService.update(profesorDTO);
                })
            );
    }

    @Override
    public Mono<NoticiaDTO> uploadFotoNoticiaToStorage(String contentType, Long noticiaId, String carpeta, FilePart filePart) {
        return uploadFileToStorageMap(carpeta, filePart).flatMap(stringObjectMap ->
            noticiaService.findOne(noticiaId).flatMap(noticiaDTO -> {
                if (noticiaDTO.getUrlFoto() != null && !noticiaDTO.getUrlFoto().isBlank()) {
                    deleteFileOfStorage(noticiaDTO.getUrlFoto())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete")).subscribe(aBoolean -> log.debug("File delete"));
                }
                noticiaDTO.setUrlFoto((String) stringObjectMap.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                return noticiaService.update(noticiaDTO);
            }));
    }

    @Override
    public Mono<EventoDTO> uploadFotoEventoToStorage(String contentType, Long eventoId, String carpeta, FilePart filePart) {
        return uploadFileToStorageMap(carpeta, filePart).flatMap(stringObjectMap ->
            eventoService.findOne(eventoId).flatMap(eventoDTO -> {
                if (eventoDTO.getUrlFoto() != null && !eventoDTO.getUrlFoto().isBlank()) {
                    deleteFileOfStorage(eventoDTO.getUrlFoto())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete")).subscribe(aBoolean -> log.debug("File delete"));
                }
                eventoDTO.setUrlFoto((String) stringObjectMap.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                return eventoService.update(eventoDTO);
            }));
    }

    @Override
    public Mono<SemilleroDTO> uploadFotoSemilleroToStorage(String contentType, Long semilleroId, String carpeta, FilePart filePart) {
        return uploadFileToStorageMap(carpeta, filePart).flatMap(stringObjectMap ->
            semilleroService.findOne(semilleroId).flatMap(semilleroDTO -> {
                if (semilleroDTO.getUrlFoto() != null && !semilleroDTO.getUrlFoto().isBlank()) {
                    deleteFileOfStorage(semilleroDTO.getUrlFoto())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete")).subscribe(aBoolean -> log.debug("File delete"));
                }
                semilleroDTO.setUrlFoto((String) stringObjectMap.get(GoogleServiceProps.PROP_NAME_FILE_UPLOAD));
                return semilleroService.update(semilleroDTO);
            }));
    }

    @Override
    public Mono<String> downloadFileFromStorage(String fileName, Long generation) {
        return Mono.just(storage).map(googleStorage -> {
            Blob blob = googleStorage.get(BlobId.of(bucketName, fileName, generation));
            return blob.reader();
        }).flatMap(this::getStreamOfFileDownloaded);
    }

    @Override
    public Mono<String> downloadFileFromStorage(String fileName) {
        return Mono.just(storage).map(googleStorage -> {
            Blob blob = googleStorage.get(BlobId.of(bucketName, fileName));
            return blob.reader();
        }).flatMap(this::getStreamOfFileDownloaded);
    }

    @Override
    public Mono<Boolean> deleteFileOfStorage(String fileName) {
        return Mono.just(storage).map(googleStorage -> {
            BlobId blobId = BlobId.of(bucketName, fileName);
            return blobId;
        }).flatMap(blobId -> {
            boolean deleteStorageResult = storage.delete(blobId);
            return Mono.just(deleteStorageResult);
        });
    }

    @Override
    public Mono<Boolean> deleteFileOfStorage(String fileName, Long generation) {
        return Mono.just(storage).map(googleStorage -> {
            BlobId blobId = BlobId.of(bucketName, fileName, generation);
            return blobId;
        }).flatMap(blobId -> {
            boolean deleteStorageResult = storage.delete(blobId);
            return Mono.just(deleteStorageResult);
        });
    }

    @Override
    public Mono<Void> deleteArchivProgramaUploaded(Long archivoProgramaId) {
        return archivosProgramaService.findOne(archivoProgramaId).flatMap(archivosProgramaDTO ->
            deleteFileOfStorage(archivosProgramaDTO.getUrlName(), archivosProgramaDTO.getGenerationStorage()))
            .flatMap(aBoolean -> archivosProgramaService.delete(archivoProgramaId));
    }

    private Mono<String> getStreamOfFileDownloaded(ReadChannel readChannel) {
        if (readChannel == null) {
            return Mono.just("");
        }
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
        String[] splitFileName = filename.split("\\.");
        if (splitFileName[0].length() > 10) {
            filename = splitFileName[0].substring(0, 10).concat(".").concat(splitFileName[splitFileName.length-1]);
        }
        String filenameNew = UUID.randomUUID().toString().concat("_")
            .concat(filename.replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9\\s.-]", "_"));
        log.debug(">>>>>>> filename: {}", filename);
        return Optional.ofNullable(directory)
            .map(s -> {
                String directoryTemp = directory.replaceAll("\\s", "-");
                return BlobId.of(bucketName, directoryTemp + "/" + filenameNew);
            })
            .orElse(BlobId.of(bucketName, filenameNew));
    }

}
