package com.publiccms.logic.dao.log;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import com.publiccms.entities.log.LogLogin;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * LogLoginD
 * 
 */
@Repository
public class LogLoginDao extends BaseDao<LogLogin> {
    
    /**
     * @param siteId
     * @param userId
     * @param startCreateDate
     * @param endCreateDate
     * @param channel
     * @param result
     * @param name
     * @param ip
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer siteId, Long userId, Date startCreateDate, Date endCreateDate, String channel,
            Boolean result, String name, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogLogin bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (null != startCreateDate) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (null != endCreateDate) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (null != result) {
            queryHandler.condition("bean.result = :result").setParameter("result", result);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", like(name));
        }
        if (notEmpty(ip)) {
            queryHandler.condition("bean.ip like :ip").setParameter("ip", like(ip));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate " + orderType + ",bean.id " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param createDate
     * @return number of data deleted
     */
    public int delete(Integer siteId, Date createDate) {
        if (notEmpty(siteId) || null != createDate) {
            QueryHandler queryHandler = getDeleteQueryHandler("from LogLogin bean");
            if (notEmpty(siteId)) {
                queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            }
            if (null != createDate) {
                queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogLogin init(LogLogin entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}