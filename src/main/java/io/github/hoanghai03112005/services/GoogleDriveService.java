package io.github.hoanghai03112005.services;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import io.github.hoanghai03112005.config.GoogleOAuthConfig;
import io.github.hoanghai03112005.models.GoogleToken;
import io.github.hoanghai03112005.repositories.GoogleTokenRepository;

@Service
public class GoogleDriveService {
	private final GoogleTokenRepository googleTokenRepository;
	
	@Autowired
    private GoogleOAuthConfig googleOAuthConfig;
	
	GoogleDriveService(GoogleTokenRepository googleTokenRepository) {
        this.googleTokenRepository = googleTokenRepository;
    }
	
	
	 public GoogleToken exchangeCodeForTokens(String code) throws IOException {
		 if (code == null || code.isEmpty()) {
		        throw new IllegalArgumentException("Mã xác thực không hợp lệ.");
		    }
		 

	        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
	                new NetHttpTransport(),
	                JacksonFactory.getDefaultInstance(),
	                "https://oauth2.googleapis.com/token",
	                googleOAuthConfig.getClientId(),
	                googleOAuthConfig.getClientSecret(),
	                code,
	                googleOAuthConfig.getRedirectUri()
	        ).execute();
	        System.out.println("Token Response: " + tokenResponse);
	        GoogleToken token = new GoogleToken();
	        token.setAccessToken(tokenResponse.getAccessToken());
	        token.setRefreshToken(tokenResponse.getRefreshToken());
	        token.setExpiryDate(Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds()));

	        googleTokenRepository.save(token);
	        return token;
	    }
	 
	 public Drive getDriveService() throws IOException {
		    GoogleToken token = googleTokenRepository.findTopByOrderByIdDesc()
		        .orElseThrow(() -> new RuntimeException("Token not found"));

		    // Nếu token hết hạn thì refresh lại
		    if (token.getExpiryDate().isBefore(Instant.now())) {
		        GoogleTokenResponse newToken = new GoogleRefreshTokenRequest(
		                new NetHttpTransport(),
		                JacksonFactory.getDefaultInstance(),
		                token.getRefreshToken(),
		                googleOAuthConfig.getClientId(),
		                googleOAuthConfig.getClientSecret()
		        ).execute();

		        token.setAccessToken(newToken.getAccessToken());
		        token.setExpiryDate(Instant.now().plusSeconds(newToken.getExpiresInSeconds()));
		        googleTokenRepository.save(token);
		    }

		    // ✅ Tạo GoogleCredentials mới với AccessToken
		    AccessToken accessToken = new AccessToken(token.getAccessToken(), java.util.Date.from(token.getExpiryDate()));
		    GoogleCredentials credentials = GoogleCredentials.create(accessToken)
		            .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

		    return new Drive.Builder(
		            new NetHttpTransport(),
		            JacksonFactory.getDefaultInstance(),
		            new HttpCredentialsAdapter(credentials)
		    ).setApplicationName("Spring Boot Drive Uploader").build();
		}

}
