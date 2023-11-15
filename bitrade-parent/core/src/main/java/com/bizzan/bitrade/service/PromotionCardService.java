package com.bizzan.bitrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bizzan.bitrade.dao.PromotionCardDao;
import com.bizzan.bitrade.dao.PromotionCardOrderDao;
import com.bizzan.bitrade.entity.Activity;
import com.bizzan.bitrade.entity.PromotionCard;
import com.bizzan.bitrade.service.Base.BaseService;
import com.querydsl.core.types.Predicate;

@Service
public class PromotionCardService extends BaseService<PromotionCard> {
	@Autowired
	private PromotionCardDao promotionCardDao;
	@Autowired
	public void setDao(PromotionCardDao dao) {
		super.setDao(dao);
	}
	@Autowired
	private PromotionCardOrderDao promotionCardOrderDao;
	
	public PromotionCard findPromotionCardByCardNo(String cardNo) {
		return promotionCardDao.findByCardNo(cardNo);
	}

	public List<PromotionCard> findAllByMemberId(Long memberId){
		return promotionCardDao.findAllByMemberId(memberId);
	}
	
	public PromotionCard findOne(Long id) {
		return promotionCardDao.getById(id);
	}
	
    public PromotionCard save(PromotionCard card) {
        return promotionCardDao.save(card);
    }

    public PromotionCard saveAndFlush(PromotionCard card) {
        return promotionCardDao.saveAndFlush(card);
    }
    
    public PromotionCard findById(Long id) {
        return promotionCardDao.getById(id);
    }

	public List<PromotionCard> findAllByMemberIdAndIsFree(long memberId, int isFree) {
		return promotionCardDao.findAllByMemberIdAndIsFree(memberId, isFree);
	}
}
