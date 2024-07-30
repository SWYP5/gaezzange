package com.swyp.gaezzange.domain.user.service;

import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;

  public Optional<User> getById(Long userId) {
    return userRepository.findById(userId);
  }

  public void deleteAll() {
    userRepository.deleteAll();
  }

  @Transactional
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public List<User> findAllByUserIds(List<Long> userIds) {
    return userRepository.findAllById(userIds);
  }

}
