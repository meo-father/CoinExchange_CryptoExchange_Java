package com.bizzan.bitrade.service;

import static com.bizzan.bitrade.entity.QCtcOrder.ctcOrder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bizzan.bitrade.dao.CtcOrderDao;
import com.bizzan.bitrade.entity.CtcOrder;
import com.bizzan.bitrade.entity.Member;
import com.bizzan.bitrade.entity.SysAdvertise;
import com.bizzan.bitrade.pagination.Criteria;
import com.bizzan.bitrade.pagination.Restrictions;
import com.bizzan.bitrade.service.Base.BaseService;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class CtcOrderService extends BaseService<CtcOrder> {
	@Autowired
    private CtcOrderDao ctcOrderDao;
    @Autowired
    public void setDao(CtcOrderDao dao) {
        super.setDao(dao);
    }
	public CtcOrder findOne(Long id) {
		return ctcOrderDao.getById(id);
	}
	
    public CtcOrder save(CtcOrder order) {
        return ctcOrderDao.save(order);
    }

    public CtcOrder saveAndFlush(CtcOrder order) {
        return ctcOrderDao.saveAndFlush(order);
    }
    
    public List<CtcOrder> findAllByStatus(int status) {
        return ctcOrderDao.findAllByStatus(status);
    }
    
    public List<CtcOrder> findAllByMemberAndStatus(Member member, int status) {
        return ctcOrderDao.findAllByMemberAndStatus(member, status);
    }
    
    public CtcOrder findById(Long id) {
        return ctcOrderDao.getById(id);
    }
    
    public List<CtcOrder> findAllByIdAndMember(Long id, Member member) {
    	return ctcOrderDao.findAllByIdAndMember(id, member);
    }

    
    public Page<CtcOrder> queryByMember(Member member, Integer pageNo, Integer pageSize){
    	//排序方式 (需要倒序 这样    Criteria.sort("id","createTime.desc") ) //参数实体类为字段名
        Sort orders = Criteria.sortStatic("createTime.desc");
        //分页参数
        PageRequest pageRequest = PageRequest.of(pageNo-1, pageSize, orders);
        //查询条件
        Criteria<CtcOrder> specification = new Criteria<CtcOrder>();

        specification.add(Restrictions.eq("member", member, false));
        
        return ctcOrderDao.findAll(specification, pageRequest);
    }
    
    public List<CtcOrder> findAll(List<Predicate> predicateList) {
        List<CtcOrder> list;
        JPAQuery<CtcOrder> jpaQuery = queryFactory.selectFrom(ctcOrder);
        if (predicateList != null) {
            jpaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
        }
        list = jpaQuery.orderBy(new OrderSpecifier<>(Order.DESC, ctcOrder.createTime)).fetch();
        return list;
    }
}
