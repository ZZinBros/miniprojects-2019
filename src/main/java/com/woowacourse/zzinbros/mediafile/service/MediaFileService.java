package com.woowacourse.zzinbros.mediafile.service;

import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.mediafile.domain.MediaFileRepository;
import com.woowacourse.zzinbros.mediafile.domain.upload.UploadTo;
import org.springframework.stereotype.Service;

@Service
public class MediaFileService {
    private final MediaFileRepository mediaFileRepository;

    public MediaFileService(MediaFileRepository mediaFileRepository) {
        this.mediaFileRepository = mediaFileRepository;
    }

    public MediaFile register(UploadTo uploadTo) {
        String url = uploadTo.save();
        return mediaFileRepository.save(new MediaFile(url));
    }
}
