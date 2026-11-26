package com.sqx.app.base.file.service.impl;

import com.sqx.app.base.file.service.FileService;
import com.sqx.common.core.domain.Result;
import com.sqx.common.file.domain.UploadRes;
import com.sqx.common.file.factory.FileStorageFactory;
import com.sqx.common.file.storage.IFileStorage;
import com.sqx.domain.db.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chengming
 * @since 2025/4/29
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private ISysConfigService iSysConfigService;

    @Override
    public Result<UploadRes> upload(MultipartFile file) {
        String fileStorageType = iSysConfigService.getConfigValue("file_storage_type");
        IFileStorage fileStorage = FileStorageFactory.getFileStorage(fileStorageType);
        UploadRes uploadRes = fileStorage.upload(file);

        return Result.success(uploadRes);
    }
}
