package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.service.GoogleDriveService;
import co.usco.facultad.ingenieria.pagina.web.rest.GoogleDriveResource;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequestInitializer;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.*;
import java.security.GeneralSecurityException;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private final Logger log = LoggerFactory.getLogger(GoogleDriveServiceImpl.class);

    @Value("${google.ruta.credenciales-file}")
    private String urlFileCredentials;

    @Override
    public void initService() throws Exception {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new FileInputStream(urlFileCredentials))
            .createScoped(Lists.newArrayList(
                "https://www.googleapis.com/auth/drive",
                "https://www.googleapis.com/auth/drive.appdata",
                "https://www.googleapis.com/auth/drive.file",
                "https://www.googleapis.com/auth/drive.metadata",
                "https://www.googleapis.com/auth/drive.photos.readonly",
                "https://www.googleapis.com/auth/drive.metadata.readonly",
                "https://www.googleapis.com/auth/drive.readonly",
                "https://www.googleapis.com/auth/drive.scripts"
            ));
            //.createDelegated("mario.serrano.developer@gmail.com");
        HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(googleCredentials);

        Drive drive = new Drive.Builder(httpTransport, jsonFactory, httpRequestInitializer)
            .setApplicationName("PaginaUscoFacultadIngenieria")
            .build();
        System.out.println("dentro de init Service");
        log.debug(" inicio data ");
        // System.out.println(drive.about().get().executeUsingHead().getStatusMessage());
        log.debug("data: {}", drive.files().list().size());
        // log.debug("dat2: {}", drive.about().toString());
        // log.debug("data3: {}", drive.about().get().toString());
        // log.debug("data4: {}", drive.about().get().setFields("user").execute().getUser());
        // log.debug("data4: {}", drive.about().get().execute().getUser());
        // log.debug("data5: {}", drive.about().get().execute().getMaxUploadSize());
        log.debug(" ----data6---- ");
        log.debug("data6: {}", drive.files().list().setQ("mimeType='application/vnd.google-apps.folder'").setSpaces("drive").setFields("nextPageToken, files(id, name)").setPageToken(null).execute().size());
        log.debug("data7: {}", drive.files().list().setQ("mimeType='image/png'").setSpaces("drive").setFields("nextPageToken, files(id, name)").setPageToken(null).execute().size());
        log.debug("data8: {}", drive.files().list().setQ("'root' in parents").setFields("nextPageToken, files(id, name)").setPageSize(10).execute());
        log.debug("data9: {}", drive.files().list().setQ("mimeType='image/png'").setFields("nextPageToken, files(id, name)").execute());

        log.debug("  ");
        log.debug("  ");


    }

    @Override
    public Mono<String> dataUpload(FilePart file) throws Exception {
        File file1 = new File("filePrueba.png");
        return file.content()
            .last()
            .map(dataBuffer -> {
            log.debug("line 1");
            // FileOutputStream fileOutputStream = (FileOutputStream) dataBuffer.asOutputStream();
            byte[] bytes = dataBuffer.asByteBuffer().array();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("pruebaFile.png");
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                log.error("error en fileOutputstream 1");
                e.printStackTrace();
            } catch (IOException e) {
                log.error("error en fileOutputstream 2");
                e.printStackTrace();
            }
            return "archivo";
        }).flatMap(s -> Mono.just(s));
        /* return file.transferTo(file1).flatMap(unused -> {
            log.debug("file: 1: {}", file1.getTotalSpace());
            return Mono.just(String.valueOf(file1.getTotalSpace()));
        }); */


        // return null;
    }
}
