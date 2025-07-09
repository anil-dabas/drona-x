package com.spot.exchange.order.model.response;

import static com.spot.exchange.order.util.Constants.SUCCESS;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultVO<T> {

    @Builder.Default
    private int code = SUCCESS;
    @Builder.Default
    private String msg = "";//StringUtils.EMPTY;
    @Builder.Default
    private List<T> data = Collections.emptyList();

    public static <T> ResultVO<T> success() {
        return ResultVO.<T>builder().code(SUCCESS).build();
    }
}
