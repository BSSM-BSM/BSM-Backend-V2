package bssm.bsm.global.bean;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OkHttpConfig {

    @Bean("okHttpClient")
    public OkHttpClient okHttpClient() {

        return new OkHttpClient();
    }
}