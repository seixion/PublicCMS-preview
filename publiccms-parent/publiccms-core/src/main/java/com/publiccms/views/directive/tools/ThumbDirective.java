package com.publiccms.views.directive.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * ThumbDirective
 * 
 */
@Component
public class ThumbDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Integer width = handler.getInteger("width");
        Integer height = handler.getInteger("height");
        SysSite site = getSite(handler);
        if (notEmpty(path) && notEmpty(width) && notEmpty(height)) {
            String thumbPath = path.substring(0, path.lastIndexOf(DOT)) + "_" + width + "_" + height
                    + fileComponent.getSuffix(path);
            File thumbFile = new File(siteComponent.getWebFilePath(site, thumbPath));
            thumbPath = site.getSitePath() + thumbPath;
            if (thumbFile.exists()) {
                handler.print(thumbPath);
            } else {
                try (FileOutputStream outputStream = new FileOutputStream(thumbFile);
                        FileLock fileLock = outputStream.getChannel().tryLock();) {
                    Thumbnails.of(siteComponent.getWebFilePath(site, path)).size(width, height).toOutputStream(outputStream);
                    handler.print(thumbPath);
                } catch (IOException e) {
                    handler.print(site.getSitePath() + path);
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private FileComponent fileComponent;

}
