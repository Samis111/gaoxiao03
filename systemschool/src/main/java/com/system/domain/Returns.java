package com.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName returns
 */
@TableName(value ="returns")
@Data
public class Returns implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userid;

    /**
     * 
     */
    private Integer projectid;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String applicantinfo;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date thistime;

    /**
     * 
     */
    private String budget;

    /**
     * 
     */
    private String existingresearchreview;

    /**
     * 
     */
    private String expectedoutcomes;

    /**
     * 
     */
    private String memberdivision;

    /**
     * 
     */
    private String projectname;

    /**
     * 
     */
    private String purposeandsignificance;

    /**
     * 
     */
    private String myreferences;

    /**
     * 
     */
    private String researchbasis;

    /**
     * 
     */
    private String researchcontent;

    /**
     * 
     */
    private String teammembers;

    /**
     * 
     */
    private String technicalroute;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Returns other = (Returns) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getProjectid() == null ? other.getProjectid() == null : this.getProjectid().equals(other.getProjectid()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getApplicantinfo() == null ? other.getApplicantinfo() == null : this.getApplicantinfo().equals(other.getApplicantinfo()))
            && (this.getThistime() == null ? other.getThistime() == null : this.getThistime().equals(other.getThistime()))
            && (this.getBudget() == null ? other.getBudget() == null : this.getBudget().equals(other.getBudget()))
            && (this.getExistingresearchreview() == null ? other.getExistingresearchreview() == null : this.getExistingresearchreview().equals(other.getExistingresearchreview()))
            && (this.getExpectedoutcomes() == null ? other.getExpectedoutcomes() == null : this.getExpectedoutcomes().equals(other.getExpectedoutcomes()))
            && (this.getMemberdivision() == null ? other.getMemberdivision() == null : this.getMemberdivision().equals(other.getMemberdivision()))
            && (this.getProjectname() == null ? other.getProjectname() == null : this.getProjectname().equals(other.getProjectname()))
            && (this.getPurposeandsignificance() == null ? other.getPurposeandsignificance() == null : this.getPurposeandsignificance().equals(other.getPurposeandsignificance()))
            && (this.getResearchbasis() == null ? other.getResearchbasis() == null : this.getResearchbasis().equals(other.getResearchbasis()))
            && (this.getResearchcontent() == null ? other.getResearchcontent() == null : this.getResearchcontent().equals(other.getResearchcontent()))
            && (this.getTeammembers() == null ? other.getTeammembers() == null : this.getTeammembers().equals(other.getTeammembers()))
            && (this.getTechnicalroute() == null ? other.getTechnicalroute() == null : this.getTechnicalroute().equals(other.getTechnicalroute()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getProjectid() == null) ? 0 : getProjectid().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getApplicantinfo() == null) ? 0 : getApplicantinfo().hashCode());
        result = prime * result + ((getThistime() == null) ? 0 : getThistime().hashCode());
        result = prime * result + ((getBudget() == null) ? 0 : getBudget().hashCode());
        result = prime * result + ((getExistingresearchreview() == null) ? 0 : getExistingresearchreview().hashCode());
        result = prime * result + ((getExpectedoutcomes() == null) ? 0 : getExpectedoutcomes().hashCode());
        result = prime * result + ((getMemberdivision() == null) ? 0 : getMemberdivision().hashCode());
        result = prime * result + ((getProjectname() == null) ? 0 : getProjectname().hashCode());
        result = prime * result + ((getPurposeandsignificance() == null) ? 0 : getPurposeandsignificance().hashCode());

        result = prime * result + ((getResearchbasis() == null) ? 0 : getResearchbasis().hashCode());
        result = prime * result + ((getResearchcontent() == null) ? 0 : getResearchcontent().hashCode());
        result = prime * result + ((getTeammembers() == null) ? 0 : getTeammembers().hashCode());
        result = prime * result + ((getTechnicalroute() == null) ? 0 : getTechnicalroute().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", projectid=").append(projectid);
        sb.append(", status=").append(status);
        sb.append(", applicantinfo=").append(applicantinfo);
        sb.append(", thistime=").append(thistime);
        sb.append(", budget=").append(budget);
        sb.append(", existingresearchreview=").append(existingresearchreview);
        sb.append(", expectedoutcomes=").append(expectedoutcomes);
        sb.append(", memberdivision=").append(memberdivision);
        sb.append(", projectname=").append(projectname);
        sb.append(", purposeandsignificance=").append(purposeandsignificance);

        sb.append(", researchbasis=").append(researchbasis);
        sb.append(", researchcontent=").append(researchcontent);
        sb.append(", teammembers=").append(teammembers);
        sb.append(", technicalroute=").append(technicalroute);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}