package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.service.CloudinaryService;
import co.usco.facultad.ingenieria.pagina.service.errors.CloudinaryException;
import com.cloudinary.Cloudinary;
import com.cloudinary.ProgressCallback;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private final Cloudinary cloudinaryConfig;

    public CloudinaryServiceImpl(Cloudinary cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    @Override
    public Mono<Map<String, Object>> uploadImageVideo(String folder, MultipartFile multipartFile) throws IOException {
        File file = convertMultipartFileToFile(multipartFile);
        Map cloudinaryResponse = cloudinaryConfig.uploader().upload(file, ObjectUtils.asMap(
            "folder", folder
        ));
        file.delete();
        return Mono.just(cloudinaryResponse);
    }

    @Override
    public Mono<Map> uploadImageVideo(String folder, FilePart filePart) {

        return convertFilePartToFile2(filePart)
            .map(s -> {
                log.debug("data return: {}", s);
                return "";
            }).flatMap(a -> {
                Map as = null;
                return Mono.just(as);
            });
        /* return convertFilePartToFile(filePart)
            .map(file -> {
                Map cloudinaryResponse = null;
                log.debug("uploadafile");
                log.debug("{}", file.length);
                try {
                    cloudinaryResponse = cloudinaryConfig.uploader().upload(filePart, ObjectUtils.emptyMap());
                } catch (IOException e) {
                    e.printStackTrace();
                    Mono.error(new CloudinaryException("Error subiendo archivo"));
                }
                // file.delete();
                return cloudinaryResponse;
            }); */
    }

    // funciona metodo subida
    @Override
    public Mono<String> uploadImageVideo(FilePart filePart) {
        File file = new File(filePart.filename());
        return filePart.transferTo(file).doOnSuccess(i -> log.debug("log success: {}", file.getTotalSpace())).then(Mono.just(file))
            .then(Mono.just(file))
            .flatMap(newFile -> {
                log.debug("newFile: {}", newFile.getTotalSpace());
                try {
                    Map cloudinaryResponse = cloudinaryConfig.uploader().upload(file, ObjectUtils.emptyMap());
                    log.debug("cloudinary: {}", cloudinaryResponse.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file.delete();
                return Mono.just(String.valueOf(newFile.getTotalSpace()));
            });
         /* return Mono.just(filePart)
            .map(f -> f.transferTo(file))
             .doOnSuccess(i -> log.debug("log success: {}", file.getTotalSpace())).then(Mono.just(file))
            .flatMap(newFile -> {
                log.debug("newFile: {}", newFile.getTotalSpace());
                return Mono.just(String.valueOf(newFile.getTotalSpace()));
            }); */
    }

    @Override
    public Mono<String> uploadImageVideo(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
            Map cloud = cloudinaryConfig.uploader().upload(bytes, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.just("algo");
    }

    private Mono<byte[]> convertPrueba(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
            .map(dataBuffer -> dataBuffer.asByteBuffer().array());
    }

    private Mono<String> convertFilePartToFile2(FilePart filePart) {
        File file = new File(filePart.filename());
        /*return filePart.content().next().map(dataBuffer -> {
            log.debug("{}", dataBuffer.readableByteCount());
            byte[] bytes = dataBuffer.asByteBuffer().array();
            log.debug("{}", bytes.length);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
                log.debug("bytes: {}", bytes[1]);
            } catch (FileNotFoundException e) {
                log.error("error en fileOutputstream 1");
                e.printStackTrace();
            } catch (IOException e) {
                log.error("error en fileOutputstream 2");
                e.printStackTrace();
            }
            return "sdas";
        }).flatMap(a -> Mono.just(file.getName())); */
        // filePart.transferTo(file);
        return filePart.transferTo(file).then().flatMap(s -> {
            log.debug("data: {}", file.getTotalSpace());
           return Mono.just(String.valueOf(file.getTotalSpace()));
        });
        // return Mono.just(String.valueOf(file.getTotalSpace()));
    }

    private Mono<byte[]> convertFilePartToFile(FilePart filePart) {
        File file = new File("prueba_subida.png");

        return filePart.content().map(dataBuffer -> {

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = dataBuffer.asInputStream();
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                int read;
                while ((read = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, read);
                }
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileItem fileItem = new DiskFileItem("mainFile", "", false, file.getName(), (int) file.getTotalSpace(), file.getParentFile());
            // return file;
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            return bytes;
        }).next();

        /*
        return filePart.map(res -> {
            // File file = new File(res.filename());

            res.transferTo(file);

            log.debug("data file");
            log.debug("{}", file.getAbsolutePath());
            log.debug("{}", file.length());
            // return file;
            return res.content().map(dataBuffer -> {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    InputStream inputStream = dataBuffer.asInputStream();
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    int read;
                    while ((read = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, read);
                    }
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }).next();
        }); */
        /*
        File file = new File(filePart.filename());
        filePart.transferTo(file);
        log.debug("antes de return");
        return Mono.just(file); */
        /*
        return filePart.content().map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            log.debug("tamaño bytes:{}", bytes.length);
            log.debug("{}", bytes[1]);
            dataBuffer.read(bytes);
            File file = new File(filePart.filename());
            FileOutputStream fileOutputStream = (FileOutputStream) dataBuffer.asOutputStream();
            try {
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        }).next();*/

    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }

}
