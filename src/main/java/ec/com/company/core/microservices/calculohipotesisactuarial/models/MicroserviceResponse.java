package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import lombok.Getter;
import lombok.Setter;

public class MicroserviceResponse<T>{

    @Getter
    @Setter
    private Integer status;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private T content;

    public MicroserviceResponse(Integer status, String message, T content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }
}
