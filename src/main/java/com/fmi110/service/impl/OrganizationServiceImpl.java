package com.fmi110.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.commons.result.Tree;
import com.fmi110.mapper.OrganizationMapper;
import com.fmi110.model.Organization;
import com.fmi110.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper,Organization> implements IOrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    /**
     * 返回给前端使用的数据 , 树
     * @return
     */
    @Override
    public List<Tree> selectTree() {
        List<Organization> organizationList = selectTreeGrid();

        List<Tree> trees = new ArrayList<Tree>();
        if (organizationList != null) {
            for (Organization organization : organizationList) {
                Tree tree = new Tree();
                tree.setId(organization.getId());
                tree.setText(organization.getName());
                tree.setIconCls(organization.getIcon());
                tree.setPid(organization.getPid());
                trees.add(tree);
            }
        }
        return trees;
    }

    @Override
    public List<Organization> selectTreeGrid() {
        EntityWrapper<Organization> w = new EntityWrapper<>();
        w.orderBy("seq");
        return organizationMapper.selectList(w);

    }
}
