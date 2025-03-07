package bssm.bsm.global.config;

import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.Security;

@Configuration
class WebPushConfig {

    @Value("${web-push.key.public}")
    private String publicKey;
    @Value("${web-push.key.private}")
    private String privateKey;

    @Bean("pushService")
    public PushService pushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        return new PushService(publicKey, privateKey);
    }
}