package com.example.restservice;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  public List<User> findByUserid(String userid);
  // findByUseridAndToken(String userid, String token);
  // findByUseridAndToken(String userid, String token);
  
}
