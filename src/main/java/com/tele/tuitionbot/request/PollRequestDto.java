package com.tele.tuitionbot.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollRequestDto implements Serializable {
    private String chat_id;
    private String question;
    private List<String> options;
    @Builder.Default
    @JsonProperty("is_anonymous")
    private boolean is_anonymous = false;
    private String type;
    private Integer correct_option_id;
}
