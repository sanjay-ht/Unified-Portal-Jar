//package com.dev.usersmanagementsystem.repository;
//
//
//import com.dev.usersmanagementsystem.entity.OurUsers;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface UsersRepo extends JpaRepository<OurUsers, Integer> {
//
//    Optional<OurUsers> findByEmail(String email);
//    @Query("SELECT u FROM OurUsers u")
//    List<OurUsers> findAllUsers();
//}
