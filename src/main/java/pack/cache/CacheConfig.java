package pack.cache;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(), 
                        Caffeine.newBuilder()
                                .expireAfterWrite(cache.getExpiredAfterWrite(), TimeUnit.HOURS)  // 캐시 유지시간 설정
                                .maximumSize(cache.getMaximumSize())  // 캐시 최대 크기 설정
                                .build()))
                .collect(Collectors.toList());  // Java 8 이상에서 안전하게 사용
    }

    @Bean
    public CacheManager cacheManager(List<CaffeineCache> caffeineCaches) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);  // Caffeine 캐시 적용
        return cacheManager;
    }
}
