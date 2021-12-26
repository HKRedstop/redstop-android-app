package com.ntredize.redstop.support.service;

import android.content.Context;

import com.ntredize.redstop.common.exception.ApplicationException;
import com.ntredize.redstop.db.model.PreloadData;
import com.ntredize.redstop.support.utils.DataUtils;
import com.ntredize.redstop.support.utils.FileUtils;
import com.ntredize.redstop.support.webservice.PreloadWebService;

import java.io.File;
import java.util.Date;

public class PreloadService {

    private final double MAX_CACHE_DAY = 7;

    private final FileUtils fileUtils;
    private final DataUtils dataUtils;
    private final RedCompanyService redCompanyService;
    private final FriendSuggestionService friendSuggestionService;
    private final PreloadWebService preloadWebService;


    /* Init */
    public PreloadService(Context context) {
        this.fileUtils = new FileUtils(context);
        this.dataUtils = new DataUtils();
        this.redCompanyService = new RedCompanyService(context);
        this.friendSuggestionService = new FriendSuggestionService(context);
        this.preloadWebService = new PreloadWebService(context);
    }


    /* Delete Cache File */
    public void deleteCacheFile() {
        File cacheFolder = fileUtils.getCacheFolder();
        deleteCacheFile(cacheFolder);
    }

    private void deleteCacheFile(File folder) {
        for (File file : fileUtils.listFolder(folder)) {
            // check is dir
            if (fileUtils.isFolder(file)) {
                deleteCacheFile(file);

                // delete empty folder
                if (fileUtils.listFolder(file).size() <= 0) {
                    fileUtils.deleteFile(file);
                }
            }
            else {
                // delete if > max cache day (7 days)
                Date lastModified = fileUtils.getFileLastModified(file);
                if (lastModified == null || dataUtils.getDayDiff(lastModified, new Date()) > MAX_CACHE_DAY) {
                    fileUtils.deleteFile(file);
                }
            }
        }
    }


    /* Get from Server */
    public PreloadData getPreloadData() throws ApplicationException {
        return preloadWebService.getPreloadData();
    }


    /* Insert Data */
    public void insertPreloadData(PreloadData preloadData) {
        // delete original data
        redCompanyService.deleteAllRedCompanyCategory();
        redCompanyService.deleteAllRedCompanySubCategory();
        friendSuggestionService.deleteAllFriendSuggestion();

        // insert data
        redCompanyService.insertRedCompanyCategory(preloadData.getRedCompanyCategories());
        redCompanyService.insertRedCompanySubCategory(preloadData.getRedCompanySubCategories());
        friendSuggestionService.insertFriendSuggestion(preloadData.getFriends());
    }

}
