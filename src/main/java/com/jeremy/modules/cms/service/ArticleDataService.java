
package com.jeremy.modules.cms.service;

import com.jeremy.modules.cms.dao.ArticleDataDao;
import com.jeremy.modules.cms.entity.ArticleData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.service.CrudService;
import com.jeremy.modules.cms.dao.ArticleDataDao;
import com.jeremy.modules.cms.entity.ArticleData;

/**
 * 站点Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

}
