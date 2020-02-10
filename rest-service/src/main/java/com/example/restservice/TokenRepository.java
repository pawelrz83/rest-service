package com.example.restservice;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {

  public List<Token> findByUserid(String userid);

}
