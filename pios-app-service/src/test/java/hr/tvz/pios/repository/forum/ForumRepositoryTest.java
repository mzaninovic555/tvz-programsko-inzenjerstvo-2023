package hr.tvz.pios.repository.forum;

import hr.tvz.pios.modul.forum.ForumRepository;
import hr.tvz.pios.modul.forum.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ForumRepositoryTest {

  @Autowired
  ForumRepository forumRepository;

  Post post = Post.builder().id(2L).title("testTitle2").content("content").createdAt(LocalDateTime.now()).authorUsername("johndoe").totalPrice(new BigDecimal("500")).build();

  @Test
  void getAllPosts(){
    List<Post> foundPost = forumRepository.getAllPosts("TestTitle");

    assertFalse(foundPost.isEmpty());
  }

  @Test
  void getById(){
    Optional<Post> foundBuild = forumRepository.getById(1L);

    assertFalse(foundBuild.isEmpty());
  }

  @Test
  @Transactional
  void insert(){
    forumRepository.insert(post);
    List<Post> posts = forumRepository.getAllPosts("");
    Post foundBuild = posts.get(posts.size()-1);

    assertTrue(forumRepository.getById(foundBuild.getId()).isPresent());
  }

  @Test
  @Transactional
  void deleteById(){
    forumRepository.deleteById(1L);

    assertTrue(forumRepository.getById(1L).isEmpty());
  }

  @Test
  void getLatestsPosts(){
    assertFalse(forumRepository.getLatestsPosts(1).isEmpty());
  }
}
