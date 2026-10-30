package urlshortener.project;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service 
public class LinkService {
    
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final int codeLength;
    private final Map<String, Link> links = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    public LinkService(@Value("${app.code-length}") int codeLength) {
        this.codeLength = codeLength;
    }

    public Link create (String url) { //create function
        while (true) {
            Link link = new Link(generateCode(), url, Instant.now());
            if(links.putIfAbsent(link.getCode(), link) ==  null) {
                return link;
            }
        }
    }

    public Optional<Link> find(String code)  {
        return Optional.ofNullable(links.get(code));
    }

    public Optional<Link> visit(String code) {
        Optional<Link> link = find(code);
        link.ifPresent(Link::addClick);
        return link;
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
           sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
