package hr.tvz.pios.repository.build;

import static org.junit.jupiter.api.Assertions.*;

import hr.tvz.pios.modul.build.Build;
import hr.tvz.pios.modul.build.BuildRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BuildRepositoryTest {

  @Autowired
  BuildRepository buildRepository;

  String linkTemp = "91b207df-692d-4dd3-b01a-e395c8ff23b7";
  Build build = Build.builder().title("titula100").description("description100").link(linkTemp).build();

  @Test
  void getById(){
    Optional<Build> foundBuild = buildRepository.getById(1L);

    assertEquals(foundBuild.get().getId(), 1L);
  }

  @Test
  void getByUserId(){
    List<Build> foundBuilds = buildRepository.getByUserId(1L);

    assertFalse(foundBuilds.isEmpty());
  }

  @Test
  @Transactional
  void insert(){
    buildRepository.insert(build);
    Optional<Build> foundBuild = buildRepository.getByLink(linkTemp);

    assertFalse(foundBuild.isEmpty());
  }

  @Test
  @Transactional
  void updateById(){
    Optional<Build> foundBuild = buildRepository.getById(1L);
    Build buildTest = Build.builder().id(foundBuild.get().getId()).title("title").link(foundBuild.get().getLink()).build();
    buildRepository.updateById(buildTest);
    assertEquals(buildRepository.getById(1L).get().getTitle(), buildTest.getTitle());
  }

  @Test
  @Transactional
  void deleteById(){
    buildRepository.deleteById(3L);
    assertTrue(buildRepository.getById(3L).isEmpty());
  }

  @Test
  void getByLink(){
    Optional<Build> buildOptional = buildRepository.getByLink(buildRepository.getById(1L).get().getLink());
    assertFalse(buildOptional.isEmpty());
  }

  @Test
  void buildHasComponent(){
    assertTrue(buildRepository.buildHasComponent(1L, 1L));
  }

  @Test
  @Transactional
  void addComponent(){
    buildRepository.addComponent(3L, 1L);
    assertTrue(buildRepository.buildHasComponent(3L, 1L));
  }

  @Test
  @Transactional
  void removeComponent(){
    buildRepository.removeComponent(1L, 1L);
    assertFalse(buildRepository.buildHasComponent(1L, 1L));
  }
}
