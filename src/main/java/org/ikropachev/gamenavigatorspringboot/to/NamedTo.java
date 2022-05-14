package org.ikropachev.gamenavigatorspringboot.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ikropachev.gamenavigatorspringboot.util.validation.NoHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class NamedTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    @Schema(example = "new-user")
    protected String name;

    public NamedTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
