package urlshortener.project;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class Link {
   
    private final String code;
    private final String url;
    private final Instant createdAt;
    private final AtomicLong clicks = new AtomicLong();

    public Link(String code, String url, Instant createAt) {
        this.code = code;
        this.url = url;
        this.createdAt = createAt;
    }

    public void addClick() {
        clicks.incrementAndGet();
    }

    public String getCode() { return code; }
    public String getUrl() { return url; }
    public Instant getCreateAt() { return createdAt; } 
    public long getClicks() { return clicks.get(); }
}
