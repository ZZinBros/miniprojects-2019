package com.woowacourse.zzinbros.post.domain.repository;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostTest;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User로 Post 리스트를 반환할 수 있는지 검증")
    void findAllByUser() {
        User savedUser = userRepository.save(new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD));
        Post post1 = new Post(PostTest.DEFAULT_CONTENT, savedUser);
        Post post2 = new Post(PostTest.DEFAULT_CONTENT, savedUser);
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> actual = postRepository.findAllByAuthor(savedUser, by(Direction.DESC, "createdDateTime"));

        assertEquals(2, actual.size());

        assertThat(actual.get(0).getContents()).isEqualTo(PostTest.DEFAULT_CONTENT);
    }

    @Test
    @DisplayName("User에 해당하는 Post가 없을 때 테스트")
    void findAllByUserWhenPostDoesntExist() {
        User savedUser = userRepository.save(new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD));
        List<Post> actual = postRepository.findAllByAuthor(savedUser, by(Direction.DESC, "createdDateTime"));

        assertEquals(0, actual.size());
    }
}