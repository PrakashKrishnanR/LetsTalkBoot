package com.app.expd.repository;

import com.app.expd.models.SubTalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface SubTalkRepository extends JpaRepository<SubTalk, Long> {

    Optional<SubTalk> findBySubtalkID(Long id);
    Optional<SubTalk> findByCommName(String commName);
}
