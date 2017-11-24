package com.voxlearning.poseidon.storage.hbase;

import com.voxlearning.poseidon.api.annotation.dao.hbase.HbaseDocument;
import com.voxlearning.poseidon.api.annotation.dao.hbase.HbaseField;
import com.voxlearning.poseidon.api.annotation.dao.hbase.HbaseRowKey;

/**
 * ${DESCRIPTION}
 *
 * @author:hao.su<hao.su@17zuoye.com>
 * @version:2017-08-10
 * @since:17-8-10
 */
@HbaseDocument(table = "homework_process_result", family = "cf")
public class HomeWork {
    @HbaseRowKey
    @HbaseField
    private String _id;

    @HbaseField
    private String actionId;

    @HbaseField
    private Long createAt;

    @HbaseField
    private Long updateAt;

    @HbaseField
    private Long finishAt;

    @HbaseField
    private Object practices;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Long finishAt) {
        this.finishAt = finishAt;
    }

    public Object getPractices() {
        return practices;
    }

    public void setPractices(Object practices) {
        this.practices = practices;
    }

    @Override
    public String toString() {
        return "HomeWork{" +
                "_id='" + _id + '\'' +
                ", actionId='" + actionId + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", finishAt=" + finishAt +
                ", practices=" + practices +
                '}';
    }
}
