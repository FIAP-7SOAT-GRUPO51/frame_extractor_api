package fiap.grupo51.fase5.frame_extractor_api.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageableCustom<T> {

    private boolean hasNext;
    private List<T> items;

    public PageableCustom(Page<T> page) {
        this.hasNext = page.hasNext();
        this.items = page.getContent();
    }
}