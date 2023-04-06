package hr.tvz.pios.repository.review;

import hr.tvz.pios.modul.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReviewRepositoryTest {

  @Autowired
  ReviewRepository reviewRepository;

  @Test
  void getById(){
    assertTrue(reviewRepository.getById(1L).isPresent());
  }

  @Test
  void getByUserAndComponentId(){
    assertTrue(reviewRepository.getByUserAndComponentId(1L, 1L).isPresent());
  }

  @Test
  @Transactional
  void deleteById(){
    reviewRepository.deleteById(1L);
    assertTrue(reviewRepository.getById(1L).isEmpty());
  }

  @Test
  @Transactional
  void deleteByUserAndComponent(){
    reviewRepository.deleteByUserAndComponent(1L,1L);
    assertTrue(reviewRepository.getByUserAndComponentId(1L,1L).isEmpty());
  }

  @Test
  void getRatingForComponent(){
    assertFalse(reviewRepository.getRatingForComponent(1L).isNaN());
  }

  @Test
  void getAllReviewedComponentsByUser(){
    assertFalse(reviewRepository.getAllReviewedComponentsByUser(1L).isEmpty());
  }
}
