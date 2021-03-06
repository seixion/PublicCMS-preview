package com.publiccms.controller.api;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.controller.api.ApiController.EXCEPTION;
import static com.publiccms.controller.api.ApiController.NEED_APP_TOKEN;
import static com.publiccms.controller.api.ApiController.NOT_FOUND_MAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.BaseMethod;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * MethodController 方法统一分发
 *
 */
@RestController
public class MethodController extends AbstractController {
    private Map<String, BaseMethod> methodMap;
    private List<Map<String, String>> methodList = new ArrayList<>();
    private ObjectWrapper objectWrapper;
    /**
     * 
     */
    public static final Map<String, String> NEED_APP_TOKEN_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(ERROR, NEED_APP_TOKEN);
        }
    };

    /**
     * 接口指令统一分发
     * 
     * @param name
     * @param appToken
     * @param request
     * @param response
     * @return result
     */
    @RequestMapping("method/{name}")
    public Object method(@PathVariable String name, String appToken, HttpServletRequest request, HttpServletResponse response) {
        BaseMethod method = methodMap.get(name);
        if (null != method) {
            try {
                if (method.needAppToken()) {
                    SysAppToken token = appTokenService.getEntity(appToken);
                    if (null == token) {
                        return NEED_APP_TOKEN_MAP;
                    }
                    SysApp app = appService.getEntity(token.getAppId());
                    if (null == app) {
                        return NEED_APP_TOKEN_MAP;
                    }
                }
                String[] paramters = request.getParameterValues("paramters");
                if (notEmpty(paramters) && paramters.length >= method.minParamtersNumber()) {
                    List<TemplateModel> list = new ArrayList<>();
                    for (String paramter : paramters) {
                        list.add(getObjectWrapper().wrap(paramter));
                    }
                    return method.exec(list);
                } else if (empty(paramters) && 0 == method.minParamtersNumber()) {
                    return method.exec(null);
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put(ERROR, "paramtersError");
                    return map;
                }
            } catch (TemplateModelException e) {
                log.error(e.getMessage(), e);
                Map<String, String> map = new HashMap<>();
                map.put(ERROR, EXCEPTION);
                return map;
            }
        } else {
            return NOT_FOUND_MAP;
        }

    }

    /**
     * 接口列表
     * 
     * @return result
     */
    @RequestMapping("methods")
    public List<Map<String, String>> methods() {
        return methodList;
    }

    /**
     * 接口初始化
     * 
     * @param directiveComponent
     * 
     */
    @Autowired
    public void init(DirectiveComponent directiveComponent) {
        methodMap = directiveComponent.getMethodMap();
        for (Entry<String, BaseMethod> entry : methodMap.entrySet()) {
            if (entry.getValue().httpEnabled()) {
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("name", entry.getKey());
                resultMap.put("minParamters", String.valueOf(entry.getValue().minParamtersNumber()));
                resultMap.put("needAppToken", String.valueOf(entry.getValue().needAppToken()));
                resultMap.put("needUserToken", String.valueOf(false));
                methodList.add(resultMap);
            }
        }
    }

    private ObjectWrapper getObjectWrapper() {
        if (null == objectWrapper) {
            objectWrapper = templateComponent.getWebConfiguration().getObjectWrapper();
        }
        return objectWrapper;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    private TemplateComponent templateComponent;
}
