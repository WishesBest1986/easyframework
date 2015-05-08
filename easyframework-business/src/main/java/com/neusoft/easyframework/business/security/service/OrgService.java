package com.neusoft.easyframework.business.security.service;

import com.neusoft.easyframework.business.security.dao.OrgDao;
import com.neusoft.easyframework.business.security.entity.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by neusoft on 15-5-7.
 */
@Service
public class OrgService {
    @Autowired
    private OrgDao orgDao;

    public void save(Org entity) {
        orgDao.save(entity);
    }

    public Org get(Long id) {
        return orgDao.get(id);
    }

    public void delete(Long id) {
        orgDao.delete(id);
    }

    public List<Org> getAll() {
        return orgDao.getAll();
    }

    public List<Org> getByParent(Long parentId) {
        if (parentId == null || parentId == Org.ROOT_ORG_ID) {
            return getAll();
        }
        return orgDao.find("from Org org where org.parentOrg = ?", new Org(parentId));
    }
}
