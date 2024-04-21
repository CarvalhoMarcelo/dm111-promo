package br.inatel.dm111promo.api.core.interceptor;

public record JwtTokenPayload(String issuer,
                              String subject,
                              String role,
                              String method,
                              String uri) {
}
