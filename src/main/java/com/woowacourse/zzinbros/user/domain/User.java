package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User extends BaseEntity {
    @Embedded
    private UserName name;

    @Embedded
    private UserPassword password;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "email", column = @Column(name = "email")))
    private UserEmail email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_file_id", foreignKey = @ForeignKey(name = "fk_user_to_media_file"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MediaFile profile;

    public User() {
    }

    public User(String name, @Email String email, String password) {
        this(name, email, password, new MediaFile(null));
    }

    public User(String name, @Email String email, String password, MediaFile profile) {
        this.name = new UserName(name);
        this.email = new UserEmail(email);
        this.password = new UserPassword(password);
        this.profile = profile;
    }

    public User update(User updatedUser) {
        this.name = updatedUser.name;
        this.email = updatedUser.email;
        this.password = updatedUser.password;
        this.profile = updatedUser.profile;
        return this;
    }

    public boolean matchPassword(String password) {
        return this.password.matchPassword(password);
    }

    public boolean isAuthor(User another) {
        return this.email.equals(another.email)
                && this.password.equals(another.password);
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public MediaFile getProfile() {
        return profile;
    }
}
