package com.castingn.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    PARTNER("ROLE_PARTNER", "파트너"),
    MEMBERSHIP("ROLE_MEMBERSHIP", "멤버십"),
    USER("ROLE_USER", "고객"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
