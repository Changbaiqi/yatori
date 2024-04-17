package com.cbq.yatori.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailInform {
    Integer sw;
    String smtpHost;
    String smtpPort;
    String email;
    String password;
}
