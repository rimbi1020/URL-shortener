package urlshortener.project;

import java.time.Instant;

import jakarta.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.concurrent.atomic.AtomicLong;

@Entity 
@Table(name = "links")
public class Link {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    private String url;
    private long clicks;
    private Instant createdAt;

    protected Link() {

    }

    public Link(String code, String url, Instant createdAt) {
        this.code = code;
        this.url = url;
        this.createdAt = createdAt;
    }

    public String getCode() { return code; }
    public String getUrl() { return url; }
    public Instant getCreateAt() { return createdAt; } 
    public long getClicks() {return clicks; }
}
