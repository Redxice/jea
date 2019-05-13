package dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ForumDto {
    private Long id;
    @NotEmpty(message = "Tag can't be empty")
    private String tag;
    @NotNull(message="A forum needs an owner")
    private Long owner_id;
    @NotEmpty(message = "A forum needs a title")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
