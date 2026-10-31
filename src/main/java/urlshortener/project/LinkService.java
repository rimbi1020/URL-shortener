package urlshortener.project;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {

    private static final String ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final LinkRepository linkRepository;
    private final int codeLength;
    private final Random random = new Random();

    public LinkService(LinkRepository linkRepository, @Value("${app.code-length}") int codeLength) {
        this.linkRepository = linkRepository;
        this.codeLength = codeLength;
    }

    @Transactional
    public Link create(String url) {
        String code;
        do {
            code = generateCode();
        } while (linkRepository.existsByCode(code));
        return linkRepository.save(new Link(code, url, Instant.now()));
    }

    @Transactional(readOnly = true)
    public Optional<Link> find(String code) {
        return linkRepository.findByCode(code);
    }

    @Transactional
    public Optional<Link> visit(String code) {
        if (linkRepository.incrementClicks(code) == 0) {
            return Optional.empty();
        }
        return linkRepository.findByCode(code);
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
