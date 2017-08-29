package org.awesky.api.auth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vme on 2017/8/22.
 */
public class Role implements Serializable {

    private Integer id;
    private Integer enable;//是否禁用角色　1　表示禁用　2　表示不禁用
    private String name;
    private String roleKey;
    private String description;
    private Set<Resources> resources = new HashSet<Resources>(0);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Resources> getResources() {
        return resources;
    }

    public void setResources(Set<Resources> resources) {
        this.resources = resources;
    }
}
