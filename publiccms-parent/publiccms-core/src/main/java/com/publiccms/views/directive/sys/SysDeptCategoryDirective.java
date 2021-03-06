package com.publiccms.views.directive.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptCategory;
import com.publiccms.entities.sys.SysDeptCategoryId;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysDeptCategoryDirective
 * 
 */
@Component
public class SysDeptCategoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        Integer categoryId = handler.getInteger("categoryId");
        if (notEmpty(deptId)) {
            if (notEmpty(categoryId)) {
                SysDept entity = sysDeptService.getEntity(deptId);
                if (null != entity) {
                    if (entity.isOwnsAllCategory()) {
                        handler.put("object", true).render();
                    } else {
                        handler.put("object", null != service.getEntity(new SysDeptCategoryId(deptId, categoryId))).render();
                    }
                }
            } else {
                Integer[] categoryIds = handler.getIntegerArray("categoryIds");
                if (notEmpty(categoryIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity) {
                        if (entity.isOwnsAllCategory()) {
                            for (Integer id : categoryIds) {
                                map.put(String.valueOf(id), true);
                            }
                        } else {
                            SysDeptCategoryId[] ids = new SysDeptCategoryId[categoryIds.length];
                            for (int i = 0; i < categoryIds.length; i++) {
                                map.put(String.valueOf(categoryIds[i]), false);
                                ids[i] = new SysDeptCategoryId(deptId, categoryIds[i]);
                            }
                            for (SysDeptCategory e : service.getEntitys(ids)) {
                                map.put(String.valueOf(e.getId().getCategoryId()), true);
                            }
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDeptCategoryService service;
    @Autowired
    private SysDeptService sysDeptService;
}
