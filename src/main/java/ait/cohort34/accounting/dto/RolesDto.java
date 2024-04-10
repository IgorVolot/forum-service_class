package ait.cohort34.accounting.dto;

import lombok.*;

import java.util.Set;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesDto {
    private String login;
    @Singular
    Set<String> roles;
}