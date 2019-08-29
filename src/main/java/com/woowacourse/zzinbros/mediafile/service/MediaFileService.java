package com.woowacourse.zzinbros.mediafile.service;

import com.woowacourse.zzinbros.common.config.upload.UploadTo;
import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.mediafile.domain.MediaFileRepository;
import org.springframework.stereotype.Service;

@Service
public class MediaFileService {
    private static final String DEFAULT_URL = "/images/default/eastjun_profile.jpg";
    private final MediaFileRepository mediaFileRepository;

    public MediaFileService(MediaFileRepository mediaFileRepository) {
        this.mediaFileRepository = mediaFileRepository;
    }

    public MediaFile register(UploadTo uploadTo) {
        String url = uploadTo.save();
        if (url == null) {
            return mediaFileRepository.save(new MediaFile(DEFAULT_URL));
        }
        return mediaFileRepository.save(new MediaFile(url));
    }
}
