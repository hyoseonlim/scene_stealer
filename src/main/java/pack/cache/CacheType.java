package pack.cache;

import lombok.Getter;

@Getter
public enum CacheType {
	
    PRODUCTS("products", 1, 1000),
	POSTS("posts", 1, 1000);
	
    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

    CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
    }
}

