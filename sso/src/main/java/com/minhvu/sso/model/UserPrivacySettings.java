package com.minhvu.sso.model;

import com.minhvu.sso.model.enums.MessagePrivacy;
import com.minhvu.sso.model.enums.ProfileVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivacySettings extends BaseEntity{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;
    @Enumerated(EnumType.STRING)
    private ProfileVisibility profileVisibility;
    private MessagePrivacy messagePrivacy;
}
