package com.example.mycoupon.domain.member;

import com.example.mycoupon.domain.oauth.entity.ProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

//    @Enumerated(EnumType.STRING)
//    private ProviderType providerType;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl;

    private String socialAccessToken;

    public void updateSocialAccessToken(String accessToken){
        this.socialAccessToken = accessToken;
    }
}
