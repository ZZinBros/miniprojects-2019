package com.woowacourse.zzinbros.mediafile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
}
