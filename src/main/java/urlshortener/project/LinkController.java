package urlshortener.project;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class LinkController {

    public record CreateLinkRequest(String url) {}
    public record CreateLinkResponse(String code) {}
    public record StatsResponce(String code, String url, long clicks, Instant createAt) {}

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/api/links")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateLinkResponse create(@RequestBody CreateLinkRequest request) {
        Link link = linkService.create(request.url());
        return new CreateLinkResponse(link.getCode());
    }

    @GetMapping ("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        Optional<Link> link = linkService.visit(code);
        if (link.isEmpty()) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(link.get().getUrl())).build();
    }

    @GetMapping("/api/links/{code}/stats")
    public ResponseEntity<StatsResponce> stats(@PathVariable String code) {
        Optional<Link> link = linkService.find(code);
        if (link.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Link l = link.get();
        return ResponseEntity.ok(new StatsResponce(l.getCode(), l.getUrl(), l.getClicks(), l.getCreateAt()));
    }
}
