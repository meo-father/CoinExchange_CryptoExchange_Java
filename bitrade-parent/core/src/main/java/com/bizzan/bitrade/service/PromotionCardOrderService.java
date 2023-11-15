package com.bizzan.bitrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bizzan.bitrade.dao.PromotionCardOrderDao;
import com.bizzan.bitrade.entity.PromotionCardOrder;
import com.bizzan.bitrade.service.Base.BaseService;
import com.querydsl.core.types.Predicate;

@Service
public class PromotionCardOrderService extends BaseService<PromotionCardOrder> {
	
	@Autowired
	private PromotionCardOrderDao promotionCardOrderDao;
	@Autowired
	public void setDao(PromotionCardOrderDao dao) {
		super.setDao(dao);
	}
	public List<PromotionCardOrder> findByCardIdAndMemberId(Long cardId, Long memberId){
		return promotionCardOrderDao.findAllByCardIdAndMemberId(cardId, memberId);
	}
	
	public List<PromotionCardOrder> findAllByCardId(Long cardId) {
		return promotionCardOrderDao.findAllByCardId(cardId);
	}
	
	public PromotionCardOrder findOne(Long id) {
		return promotionCardOrderDao.getById(id);
	}
	
    public PromotionCardOrder save(PromotionCardOrder order) {
        return promotionCardOrderDao.save(order);
    }

    public PromotionCardOrder saveAndFlush(PromotionCardOrder order) {
        return promotionCardOrderDao.saveAndFlush(order);
    }
    
    public PromotionCardOrder findById(Long id) {
        return promotionCardOrderDao.getById(id);
    }

	public List<PromotionCardOrder> findAllByMemberIdAndIsFree(long memberId, int isFree) {
		return promotionCardOrderDao.findAllByMemberIdAndIsFree(memberId, isFree);
	}
}
