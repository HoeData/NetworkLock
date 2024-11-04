package com.ruoyi.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysMenu;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 
 * @author ruoyi
 */
public class CompanyTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CompanyTreeSelect> children;

    public CompanyTreeSelect()
    {

    }

    public CompanyTreeSelect(LockCompany company)
    {
        this.id = Long.valueOf(company.getId());
        this.label = company.getName();
        this.children = company.getChildren().stream().map(CompanyTreeSelect::new).collect(Collectors.toList());
    }



    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<CompanyTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<CompanyTreeSelect> children)
    {
        this.children = children;
    }
}
