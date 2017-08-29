package org.awesky.provider.dao.auth;

import org.apache.ibatis.annotations.Select;
import org.awesky.api.auth.Resources;

import java.util.List;

/**
 * Created by vme on 2017/8/22.
 */
public interface AuthResourcesMapper {

    @Select({"Select id,res_name name,res_Key resKey,res_Url resUrl from tb_resources"})
    List<Resources> findAll();

    @Select({"SELECT id id,res_name name,res_role_id roleId,res_key resKey,res_type type,res_url resUrl,",
            "res_level level,res_descr description FROM tb_resources WHERE res_key=#{name,jdbcType=VARCHAR}"})
    List<Resources> getUserResources(String valueOf);

}
