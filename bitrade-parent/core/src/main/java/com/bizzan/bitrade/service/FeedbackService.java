package com.bizzan.bitrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizzan.bitrade.dao.FeedbackDao;
import com.bizzan.bitrade.entity.Feedback;
import com.bizzan.bitrade.service.Base.BaseService;

/**
 * @author GS
 * @date 2018年03月19日
 */
@Service
public class FeedbackService extends BaseService<Feedback> {
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    public void setDao(FeedbackDao dao) {
        super.setDao(dao);
    }
    public Feedback save(Feedback feedback){
        return feedbackDao.save(feedback);
    }
}
