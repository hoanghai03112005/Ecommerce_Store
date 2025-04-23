package io.github.hoanghai03112005.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Permission;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriveService {

    private static final String APPLICATION_NAME = "Spring Boot Drive Uploader";
    private static final JsonFactory JSON_FACTORY = Utils.getDefaultJsonFactory();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials/credentials.json";
    private final GoogleDriveService googleDriveService;
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/drive.file");
    
    public DriveService(GoogleDriveService googleDriveService) {
		// TODO Auto-generated constructor stub
    	this.googleDriveService = googleDriveService;
	}

    // Kết nối với Google Drive Service
    public static Drive getDriveService() throws Exception {
        final var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        var in = DriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new RuntimeException("Không tìm thấy file credentials.json");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Xây dựng GoogleAuthorizationCodeFlow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // Lấy token của người dùng
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user");

        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Upload tệp ZIP lên Google Drive
    public String uploadFileToDrive(String zipFilePath) {
        try {

            Drive driveService =  googleDriveService.getDriveService(); // Bắt lỗi ở đây
            File fileMetadata = new File();
            fileMetadata.setName("uploaded-zip-file.zip");

            java.io.File filePath = new java.io.File(zipFilePath);
            FileContent mediaContent = new FileContent("application/zip", filePath);

            File file = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webViewLink")
                    .execute();
   
            return file.getWebViewLink();
        } catch (Exception e) {
            e.printStackTrace(); // In ra chi tiết lỗi nếu có
            return "Lỗi khi upload file: " + e.getMessage();
        }
    }

}

