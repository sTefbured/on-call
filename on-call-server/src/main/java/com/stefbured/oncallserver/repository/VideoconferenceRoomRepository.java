package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoconferenceRoomRepository extends JpaRepository<VideoconferenceRoom, Long> {
    Page<VideoconferenceRoom> findAllByCreatorId(Long creatorId, Pageable pageable);
}
