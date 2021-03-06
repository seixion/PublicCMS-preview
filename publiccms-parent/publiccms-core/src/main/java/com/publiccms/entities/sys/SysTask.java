package com.publiccms.entities.sys;

import static com.publiccms.common.database.CmsUpgrader.IDENTIFIER_GENERATOR;

import java.util.Date;

// Generated 2016-1-19 11:28:06 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * SystemTask generated by hbm2java
 */
@Entity
@Table(name = "sys_task")
public class SysTask implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @GeneratorColumn(title = "ID")
    private Integer id;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private int siteId;
    @GeneratorColumn(title = "任务名称")
    private String name;
    @GeneratorColumn(title = "状态", condition = true)
    private int status;
    @GeneratorColumn(title = "表达式")
    private String cronExpression;
    @GeneratorColumn(title = "描述")
    private String description;
    @GeneratorColumn(title = "任务内容")
    private String filePath;
    @GeneratorColumn(title = "更新日期", condition = true)
    private Date updateDate;

    public SysTask() {
    }

    public SysTask(int siteId, String name, int status, String cronExpression) {
        this.siteId = siteId;
        this.name = name;
        this.status = status;
        this.cronExpression = cronExpression;
    }

    public SysTask(int siteId, String name, int status, String cronExpression, String description, String filePath,
            Date updateDate) {
        this.siteId = siteId;
        this.name = name;
        this.status = status;
        this.cronExpression = cronExpression;
        this.description = description;
        this.filePath = filePath;
        this.updateDate = updateDate;
    }

    @Id
    @GeneratedValue(generator = "cmsGenerator")
    @GenericGenerator(name = "cmsGenerator", strategy = IDENTIFIER_GENERATOR)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "site_id", nullable = false)
    public int getSiteId() {
        return this.siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "cron_expression", nullable = false, length = 50)
    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Column(name = "description", length = 300)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "file_path")
    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
