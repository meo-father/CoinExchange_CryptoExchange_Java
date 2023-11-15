package com.bizzan.bitrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bizzan.bitrade.dao.CtcAcceptorDao;
import com.bizzan.bitrade.entity.CtcAcceptor;
import com.bizzan.bitrade.entity.Member;
import com.bizzan.bitrade.service.Base.BaseService;
import com.querydsl.core.types.Predicate;

@Service
public class CtcAcceptorService extends BaseService<CtcAcceptor> {
	@Autowired
    private CtcAcceptorDao ctcAcceptorDao;
    @Autowired
    public void setDao(CtcAcceptorDao dao) {
        super.setDao(dao);
    }
	public CtcAcceptor findOne(Long id) {
		return ctcAcceptorDao.getById(id);
	}
	
    public CtcAcceptor save(CtcAcceptor acceptor) {
        return ctcAcceptorDao.save(acceptor);
    }

    public CtcAcceptor saveAndFlush(CtcAcceptor acceptor) {
        return ctcAcceptorDao.saveAndFlush(acceptor);
    }
    
    public CtcAcceptor findById(Long id) {
        return ctcAcceptorDao.getById(id);
    }
    
    public List<CtcAcceptor> findByStatus(int status) {
        return ctcAcceptorDao.findAllByStatus(status);
    }

	public List<CtcAcceptor> findByMember(Member acceptor) {
		return ctcAcceptorDao.findAllByMember(acceptor);
	}
}
